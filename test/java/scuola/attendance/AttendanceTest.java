package scuola.attendance;

import org.junit.jupiter.api.Test;
import scuola.lessons.Lesson;
import scuola.users.Student;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AttendanceTest {

    @Test
    void constructorStoresAllFields() {
        LocalDate date = LocalDate.of(2026, 6, 18);
        Student student = new Student("Jan", "Kowalski");
        Lesson lesson = new Lesson("Matematyka", null, null);

        Attendance attendance = new Attendance(date, true, student, lesson);

        assertEquals(date, attendance.getDate());
        assertTrue(attendance.isPresent());
        assertSame(student, attendance.getStudent());
        assertSame(lesson, attendance.getLesson());
    }

    @Test
    void presenceCanBeToggled() {
        Attendance attendance = new Attendance(LocalDate.now(), true);
        attendance.setPresent(false);
        assertFalse(attendance.isPresent());
    }
}
