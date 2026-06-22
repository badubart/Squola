package scuola.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import scuola.fixtures.TestFixtures;
import scuola.lessons.Lesson;
import scuola.lessons.Reservation;
import scuola.lessons.ReservationService;
import scuola.lessons.ReservationStatus;
import scuola.notifications.NotificationService;
import scuola.notifications.NotificationType;
import scuola.repository.LessonRepository;
import scuola.repository.ReservationRepository;
import scuola.users.Student;
import scuola.users.Teacher;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Scenariusz akceptacyjny: rezerwacja zajęć i powiadomienia")
class ReservationAcceptanceTest {

    private LessonRepository lessonRepository;
    private ReservationService reservationService;
    private Teacher teacher;

    @BeforeEach
    void setUp() {
        lessonRepository = new LessonRepository();
        NotificationService notifications = new NotificationService();
        reservationService = new ReservationService(lessonRepository, new ReservationRepository(), notifications);
        teacher = TestFixtures.teacher();
    }

    @Test
    @DisplayName("Uczeń rezerwuje zajęcia i otrzymuje potwierdzenie SMS")
    void studentReservesLessonAndReceivesConfirmation() {
        // Given: zaplanowane zajęcia z wolnymi miejscami
        Lesson lesson = TestFixtures.lesson(teacher);
        lessonRepository.save(lesson);
        Student student = TestFixtures.student();

        // When: uczeń rezerwuje zajęcia
        boolean result = reservationService.createReservation(student, lesson);

        // Then: rezerwacja przyjęta, uczeń zapisany i powiadomiony SMS-em
        assertTrue(result);
        assertEquals(1, lesson.getEnrolledStudents().size());
        assertEquals(1, student.getNotifications().size());
        assertEquals(NotificationType.SMS, student.getNotifications().get(0).getType());
    }

    @Test
    @DisplayName("Rezerwacja jest odrzucana, gdy brak wolnych miejsc")
    void reservationRejectedWhenNoSeats() {
        // Given: zajęcia z jednym miejscem, już zajętym
        Lesson lesson = TestFixtures.lesson(teacher);
        lesson.setMaxSeats(1);
        lessonRepository.save(lesson);
        assertTrue(reservationService.createReservation(TestFixtures.student("Jan", "Kowalski"), lesson));

        // When: kolejny uczeń próbuje zarezerwować
        boolean second = reservationService.createReservation(TestFixtures.student("Ewa", "Lis"), lesson);

        // Then: odmowa, brak wolnych miejsc
        assertFalse(second);
        assertEquals(0, lesson.getAvailableSeats());
    }

    @Test
    @DisplayName("Rezerwację można anulować na więcej niż 24h przed zajęciami")
    void reservationCancelledMoreThan24hBefore() {
        // Given: rezerwacja na zajęcia za 2 dni
        Lesson lesson = TestFixtures.lesson(teacher);
        lessonRepository.save(lesson);
        Student student = TestFixtures.student();
        reservationService.createReservation(student, lesson);
        Reservation reservation = student.getReservations().get(0);

        // When: uczeń anuluje rezerwację
        boolean cancelled = reservationService.cancelReservation(reservation);

        // Then: rezerwacja anulowana, miejsce zwolnione
        assertTrue(cancelled);
        assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
        assertEquals(lesson.getMaxSeats(), lesson.getAvailableSeats());
    }

    @Test
    @DisplayName("Anulowanie poniżej 24h przed zajęciami jest odrzucane")
    void reservationCancellationRejectedUnder24h() {
        // Given: rezerwacja na zajęcia za 2 godziny
        Lesson lesson = TestFixtures.lesson(teacher);
        lesson.setStartTime(LocalDateTime.now().plusHours(2));
        lessonRepository.save(lesson);
        Student student = TestFixtures.student();
        reservationService.createReservation(student, lesson);
        Reservation reservation = student.getReservations().get(0);

        // When: uczeń próbuje anulować
        boolean cancelled = reservationService.cancelReservation(reservation);

        // Then: odmowa, rezerwacja nadal potwierdzona
        assertFalse(cancelled);
        assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());
    }
}
