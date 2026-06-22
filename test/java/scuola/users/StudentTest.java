package scuola.users;

import org.junit.jupiter.api.Test;
import scuola.attendance.Attendance;
import scuola.attendance.ProgressReport;
import scuola.lessons.Reservation;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StudentTest {

    @Test
    void smsReminderIsDisabledByDefaultAndCanBeToggled() {
        Student student = new Student("Jan", "Kowalski");
        assertFalse(student.isSmsReminderEnabled());

        student.setSmsReminderEnabled(true);
        assertTrue(student.isSmsReminderEnabled());
    }

    @Test
    void parentPhoneNumberCanBeSet() {
        Student student = new Student("Jan", "Kowalski");
        student.setParentPhoneNumber("600300400");
        assertEquals("600300400", student.getParentPhoneNumber());
    }

    @Test
    void inheritsFullNameFromUser() {
        Student student = new Student("Jan", "Kowalski");
        assertEquals("Jan Kowalski", student.getFullName());
    }

    @Test
    void reservationsAreCollectedAndReadOnly() {
        Student student = new Student("Jan", "Kowalski");
        student.addReservation(new Reservation(student, null));

        assertEquals(1, student.getReservations().size());
        assertThrows(UnsupportedOperationException.class,
                () -> student.getReservations().add(new Reservation(student, null)));
    }

    @Test
    void attendanceAndProgressReportsAreCollected() {
        Student student = new Student("Jan", "Kowalski");
        student.addAttendance(new Attendance(LocalDate.now(), true));
        student.addProgressReport(new ProgressReport("Postępy", LocalDate.now()));

        assertEquals(1, student.getAttendanceRecords().size());
        assertEquals(1, student.getProgressReports().size());
    }
}
