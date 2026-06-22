package scuola.attendance;

import org.junit.jupiter.api.Test;
import scuola.lessons.Lesson;
import scuola.users.Student;
import scuola.users.Teacher;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ProgressReportTest {

    @Test
    void constructorStoresAllFields() {
        LocalDate date = LocalDate.of(2026, 6, 18);
        Student student = new Student("Jan", "Kowalski");
        Teacher author = new Teacher("Anna", "Nowak");
        Lesson lesson = new Lesson("Matematyka", null, null);

        ProgressReport report = new ProgressReport("Postępy", date, student, author, lesson);

        assertEquals("Postępy", report.getContent());
        assertEquals(date, report.getDate());
        assertSame(student, report.getStudent());
        assertSame(author, report.getAuthor());
        assertSame(lesson, report.getLesson());
    }

    @Test
    void contentCanBeUpdated() {
        ProgressReport report = new ProgressReport("Stara", LocalDate.now());
        report.setContent("Nowa");
        assertEquals("Nowa", report.getContent());
    }
}
