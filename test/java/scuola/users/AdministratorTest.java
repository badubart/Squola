package scuola.users;

import org.junit.jupiter.api.Test;
import scuola.attendance.Attendance;
import scuola.lessons.Lesson;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdministratorTest {

    @Test
    void canEditLessonDetails() {
        Administrator admin = new Administrator("Adam", "Admin");
        Lesson lesson = new Lesson("Matematyka", null, null);

        admin.editLessonDetails(lesson, "Fizyka");

        assertEquals("Fizyka", lesson.getSubject());
    }

    @Test
    void canEditAttendance() {
        Administrator admin = new Administrator("Adam", "Admin");
        Attendance attendance = new Attendance(LocalDate.now(), true);

        admin.editAttendance(attendance, false);
        assertFalse(attendance.isPresent());

        admin.editAttendance(attendance, true);
        assertTrue(attendance.isPresent());
    }

    @Test
    void isAUser() {
        Administrator admin = new Administrator("Adam", "Admin");
        assertEquals("Adam Admin", admin.getFullName());
        assertTrue(admin instanceof User);
    }
}
