package scuola.lessons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scuola.attendance.UploadedFile;
import scuola.fixtures.TestFixtures;
import scuola.notifications.NotificationService;
import scuola.notifications.NotificationType;
import scuola.repository.LessonRepository;
import scuola.repository.ReservationRepository;
import scuola.users.Student;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReservationServiceTest {

    private LessonRepository lessonRepository;
    private ReservationService service;

    @BeforeEach
    void setUp() {
        lessonRepository = new LessonRepository();
        service = new ReservationService(lessonRepository, new ReservationRepository(), new NotificationService());
    }

    private Lesson scheduledLesson(String subject, int maxSeats, LocalDateTime start) {
        Lesson lesson = new Lesson(subject, start, start.plusHours(1), TestFixtures.teacher(), maxSeats);
        lessonRepository.save(lesson);
        return lesson;
    }

    @Test
    void getAvailableLessonsReturnsScheduledWithSeats() {
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        scheduledLesson("Matematyka", 2, start);
        scheduledLesson("Fizyka", 2, start);
        scheduledLesson("Matematyka", 0, start);          // brak miejsc
        Lesson cancelled = scheduledLesson("Matematyka", 2, start);
        cancelled.setStatus(LessonStatus.CANCELLED);

        assertEquals(2, service.getAvailableLessons(null).size());
        assertEquals(1, service.getAvailableLessons("mate").size());
    }

    @Test
    void createReservationSucceedsAndNotifies() {
        Lesson lesson = scheduledLesson("Matematyka", 2, LocalDateTime.now().plusDays(2));
        Student student = TestFixtures.student();

        assertTrue(service.createReservation(student, lesson));
        assertEquals(1, lesson.getEnrolledStudents().size());
        assertEquals(NotificationType.SMS, student.getNotifications().get(0).getType());
    }

    @Test
    void createReservationFailsWhenNoSeats() {
        Lesson lesson = scheduledLesson("Matematyka", 0, LocalDateTime.now().plusDays(2));
        assertFalse(service.createReservation(TestFixtures.student(), lesson));
    }

    @Test
    void cancelReservationRespects24hRule() {
        Lesson early = scheduledLesson("Matematyka", 2, LocalDateTime.now().plusHours(2));
        Lesson later = scheduledLesson("Matematyka", 2, LocalDateTime.now().plusDays(2));
        Student student = TestFixtures.student();
        service.createReservation(student, early);
        service.createReservation(student, later);

        Reservation earlyReservation = early.getReservations().get(0);
        Reservation laterReservation = later.getReservations().get(0);

        assertFalse(service.cancelReservation(earlyReservation)); // < 24h
        assertTrue(service.cancelReservation(laterReservation));   // > 24h
        assertEquals(ReservationStatus.CANCELLED, laterReservation.getStatus());
    }

    @Test
    void createReservationsForRangeCreatesPerLesson() {
        LocalDate today = LocalDate.now();
        scheduledLesson("Matematyka", 2, today.atTime(10, 0));
        scheduledLesson("Fizyka", 2, today.plusDays(1).atTime(10, 0));

        List<Reservation> created = service.createReservations(TestFixtures.student(), today, today.plusDays(1));

        assertEquals(2, created.size());
        assertEquals(2, service.countLessons(today, today.plusDays(1)));
    }

    @Test
    void createReservationsRejectsInvalidRange() {
        LocalDate today = LocalDate.now();
        assertThrows(IllegalArgumentException.class,
                () -> service.createReservations(TestFixtures.student(), today.plusDays(2), today));
    }

    @Test
    void exportToCalendarProducesIcsFile() {
        Lesson lesson = scheduledLesson("Matematyka", 2, LocalDateTime.now().plusDays(2));
        Student student = TestFixtures.student();
        service.createReservation(student, lesson);

        UploadedFile file = service.exportToCalendar(student);

        assertTrue(file.getFileName().endsWith(".ics"));
        assertTrue(file.getContent().contains("Matematyka"));
    }

    @Test
    void getAvailableLessonsFiltersByMaxPrice() {
        Lesson cheap = scheduledLesson("Matematyka", 2, LocalDateTime.now().plusDays(2));
        cheap.setPrice(new BigDecimal("50"));
        Lesson expensive = scheduledLesson("Matematyka", 2, LocalDateTime.now().plusDays(2));
        expensive.setPrice(new BigDecimal("200"));

        List<Lesson> withinBudget = service.getAvailableLessons(null, new BigDecimal("60"));

        assertEquals(1, withinBudget.size());
        assertTrue(withinBudget.contains(cheap));
    }

    @Test
    void exportToCalendarReturnsNullWhenNoReservations() {
        assertNull(service.exportToCalendar(TestFixtures.student()));
    }
}
