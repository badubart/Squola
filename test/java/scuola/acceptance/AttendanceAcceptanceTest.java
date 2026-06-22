package scuola.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import scuola.attendance.AttendanceService;
import scuola.fixtures.TestFixtures;
import scuola.lessons.Lesson;
import scuola.users.Student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Scenariusz akceptacyjny: ewidencja obecności i próg nieobecności")
class AttendanceAcceptanceTest {

    private AttendanceService attendanceService;

    @BeforeEach
    void setUp() {
        attendanceService = new AttendanceService();
    }

    @Test
    @DisplayName("Przekroczenie progu nieobecności automatycznie powiadamia ucznia")
    void exceedingAbsenceLimitNotifiesStudent() {
        // Given: limit 1 nieobecności
        attendanceService.setAbsenceLimit(1);
        Lesson lesson = TestFixtures.lesson();
        Student student = TestFixtures.studentWithSms("Jan", "Kowalski");

        // When: uczeń jest nieobecny dwukrotnie
        attendanceService.markAttendance(lesson, student, false);
        attendanceService.markAttendance(lesson, student, false);

        // Then: próg przekroczony, powiadomienie wysłane automatycznie (jednokrotnie)
        assertTrue(attendanceService.checkTooManyAbsences(student));
        assertEquals(2, lesson.getAttendances().size());
        assertEquals(1, student.getNotifications().size());
    }

    @Test
    @DisplayName("Liczba nieobecności równa limitowi nie wyzwala powiadomienia")
    void atLimitDoesNotNotify() {
        attendanceService.setAbsenceLimit(2);
        Lesson lesson = TestFixtures.lesson();
        Student student = TestFixtures.student();

        attendanceService.markAttendance(lesson, student, false);
        attendanceService.markAttendance(lesson, student, false);

        assertFalse(attendanceService.checkTooManyAbsences(student));
        assertTrue(student.getNotifications().isEmpty());
    }

    @Test
    @DisplayName("Lista obecności zawiera uczniów zapisanych na zajęcia")
    void rosterContainsEnrolledStudents() {
        // Given: dwoje uczniów zapisanych przez rezerwację
        Lesson lesson = TestFixtures.lesson();
        Student first = TestFixtures.student("Jan", "Kowalski");
        Student second = TestFixtures.student("Ewa", "Lis");
        TestFixtures.enrol(lesson, first);
        TestFixtures.enrol(lesson, second);

        // Then: lista uczniów do sprawdzenia obecności ma dwie osoby
        assertEquals(2, attendanceService.listStudentsForLesson(lesson).size());
    }
}
