package scuola.attendance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scuola.lessons.Lesson;
import scuola.lessons.Reservation;
import scuola.users.Student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AttendanceServiceTest {

    private AttendanceService service;
    private Lesson lesson;
    private Student student;

    @BeforeEach
    void setUp() {
        service = new AttendanceService();
        lesson = new Lesson("Matematyka", null, null);
        student = new Student("Jan", "Kowalski");
    }

    @Test
    void markAttendanceRecordsStatusOnLessonAndStudent() {
        boolean result = service.markAttendance(lesson, student, true);

        assertTrue(result);
        assertEquals(1, lesson.getAttendances().size());
        assertEquals(1, student.getAttendanceRecords().size());
    }

    @Test
    void markAttendanceRejectsNullArguments() {
        assertFalse(service.markAttendance(null, student, true));
        assertFalse(service.markAttendance(lesson, null, true));
    }

    @Test
    void updateAttendanceChangesPresence() {
        Attendance attendance = new Attendance(java.time.LocalDate.now(), true);

        assertTrue(service.updateAttendance(attendance, false));
        assertFalse(attendance.isPresent());
    }

    @Test
    void listStudentsForLessonReturnsEnrolledStudents() {
        Student other = new Student("Ewa", "Lis");
        enrol(student);
        enrol(other);

        assertEquals(2, service.listStudentsForLesson(lesson).size());
    }

    @Test
    void absenceLimitCanBeConfigured() {
        service.setAbsenceLimit(5);
        assertEquals(5, service.getAbsenceLimit());
    }

    @Test
    void checkTooManyAbsencesRespectsLimit() {
        service.setAbsenceLimit(2);
        service.markAttendance(lesson, student, false);
        service.markAttendance(lesson, student, false);
        assertFalse(service.checkTooManyAbsences(student)); // 2 nie przekracza 2

        service.markAttendance(lesson, student, false);
        assertTrue(service.checkTooManyAbsences(student)); // 3 > 2
    }

    private void enrol(Student enrolled) {
        Reservation reservation = new Reservation(enrolled, lesson);
        reservation.confirm();
        lesson.addReservation(reservation);
    }
}
