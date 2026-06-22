package scuola.lessons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scuola.fixtures.TestFixtures;
import scuola.notifications.NotificationService;
import scuola.repository.LessonRepository;
import scuola.repository.RoomRepository;
import scuola.repository.UserRepository;
import scuola.users.Student;
import scuola.users.Teacher;
import scuola.users.TeacherAvailability;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScheduleServiceTest {

    private LessonRepository lessonRepository;
    private RoomRepository roomRepository;
    private UserRepository userRepository;
    private ScheduleService service;

    private final LocalDateTime start = LocalDateTime.of(2026, 6, 22, 10, 0); // poniedziałek
    private final LocalDateTime end = start.plusHours(1);

    @BeforeEach
    void setUp() {
        lessonRepository = new LessonRepository();
        roomRepository = new RoomRepository();
        userRepository = new UserRepository();
        service = new ScheduleService(lessonRepository, roomRepository, userRepository, new NotificationService());
    }

    @Test
    void listLessonsReturnsAllSavedLessons() {
        lessonRepository.save(new Lesson("Matematyka", start, end));
        lessonRepository.save(new Lesson("Fizyka", start, end));

        assertEquals(2, service.listLessons().size());
    }

    @Test
    void listLessonsByTeacherFiltersByTeacher() {
        Teacher teacher = TestFixtures.teacher();
        Lesson own = new Lesson("Matematyka", start, end, teacher);
        lessonRepository.save(own);
        lessonRepository.save(new Lesson("Fizyka", start, end, TestFixtures.teacher()));

        List<Lesson> lessons = service.listLessons(teacher);

        assertEquals(1, lessons.size());
        assertSame(own, lessons.get(0));
    }

    @Test
    void assignRoomSucceedsWhenRoomFree() {
        Lesson lesson = new Lesson("Matematyka", start, end);
        lessonRepository.save(lesson);
        Room room = TestFixtures.room();

        assertTrue(service.assignRoom(lesson, room));
        assertSame(room, lesson.getRoom());
    }

    @Test
    void assignRoomFailsWhenRoomBusyAtSameTime() {
        Room room = TestFixtures.room();
        Lesson first = new Lesson("Matematyka", start, end);
        Lesson second = new Lesson("Fizyka", start, end);
        lessonRepository.save(first);
        lessonRepository.save(second);

        assertTrue(service.assignRoom(first, room));
        assertFalse(service.assignRoom(second, room));
    }

    @Test
    void listAvailableRoomsExcludesBusyRooms() {
        Room free = new Room("Wolna", 10, "");
        Room busy = new Room("Zajeta", 10, "");
        roomRepository.save(free);
        roomRepository.save(busy);
        Lesson lesson = new Lesson("Matematyka", start, end);
        lesson.setRoom(busy);
        lessonRepository.save(lesson);

        List<Room> available = service.listAvailableRooms(start, end);

        assertEquals(1, available.size());
        assertSame(free, available.get(0));
    }

    @Test
    void substituteTeacherChangesTeacher() {
        Lesson lesson = new Lesson("Matematyka", start, end, TestFixtures.teacher());
        Teacher replacement = TestFixtures.teacher();

        assertTrue(service.substituteTeacher(lesson, replacement));
        assertSame(replacement, lesson.getTeacher());
    }

    @Test
    void rescheduleLessonChangesTimes() {
        Lesson lesson = new Lesson("Matematyka", start, end);
        LocalDateTime newStart = start.plusDays(1);

        assertTrue(service.rescheduleLesson(lesson, newStart, newStart.plusHours(1)));
        assertEquals(newStart, lesson.getStartTime());
    }

    @Test
    void cancelLessonMarksCancelledAndNotifiesParticipants() {
        Teacher teacher = TestFixtures.teacher();
        Lesson lesson = new Lesson("Matematyka", start, end, teacher);
        Student student = TestFixtures.student();
        TestFixtures.enrol(lesson, student);
        lessonRepository.save(lesson);

        assertTrue(service.cancelLesson(lesson, "Choroba nauczyciela"));

        assertEquals(LessonStatus.CANCELLED, lesson.getStatus());
        assertEquals(ReservationStatus.CANCELLED, lesson.getReservations().get(0).getStatus());
        assertEquals(1, teacher.getNotifications().size());
        assertEquals(1, student.getNotifications().size());
    }

    @Test
    void listAvailableTeachersReturnsFreeTeachersWithMatchingAvailability() {
        Teacher available = TestFixtures.teacher();
        available.addAvailability(new TeacherAvailability(start.getDayOfWeek(),
                start.toLocalTime().minusHours(2), end.toLocalTime().plusHours(2)));
        userRepository.save(available);

        Teacher busy = TestFixtures.teacher();
        busy.addAvailability(new TeacherAvailability(start.getDayOfWeek(),
                start.toLocalTime().minusHours(2), end.toLocalTime().plusHours(2)));
        userRepository.save(busy);
        Lesson busyLesson = new Lesson("Matematyka", start, end, busy);
        lessonRepository.save(busyLesson);

        List<Teacher> result = service.listAvailableTeachers("Matematyka", start, end);

        assertTrue(result.contains(available));
        assertFalse(result.contains(busy));
    }
}
