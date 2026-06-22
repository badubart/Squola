package scuola.attendance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scuola.lessons.Lesson;
import scuola.users.Student;
import scuola.users.Teacher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProgressReportServiceTest {

    private ProgressReportService service;
    private Student student;
    private Teacher teacher;
    private Lesson lesson;

    @BeforeEach
    void setUp() {
        service = new ProgressReportService();
        teacher = new Teacher("Anna", "Nowak");
        student = new Student("Jan", "Kowalski");
        lesson = new Lesson("Matematyka", null, null, teacher);
    }

    @Test
    void createLinksReportToStudentAndAuthor() {
        ProgressReport report = service.createProgressReport(student, lesson, "Świetne postępy");

        assertEquals("Świetne postępy", report.getContent());
        assertSame(student, report.getStudent());
        assertSame(teacher, report.getAuthor());
        assertEquals(1, student.getProgressReports().size());
        assertEquals(1, teacher.getCreatedProgressReports().size());
    }

    @Test
    void createRejectsEmptyContent() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createProgressReport(student, lesson, "   "));
    }

    @Test
    void createRejectsMissingStudentOrLesson() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createProgressReport(null, lesson, "x"));
        assertThrows(IllegalArgumentException.class,
                () -> service.createProgressReport(student, null, "x"));
    }

    @Test
    void updateChangesContentForStoredReport() {
        ProgressReport report = service.createProgressReport(student, lesson, "Stara treść");

        assertTrue(service.updateProgressReport(report, "Nowa treść"));
        assertEquals("Nowa treść", report.getContent());
    }

    @Test
    void updateRejectsEmptyContent() {
        ProgressReport report = service.createProgressReport(student, lesson, "Treść");
        assertFalse(service.updateProgressReport(report, ""));
    }

    @Test
    void updateRejectsUnknownReport() {
        ProgressReport external = new ProgressReport("Treść", null);
        assertFalse(service.updateProgressReport(external, "Nowa"));
    }

    @Test
    void deleteRemovesReport() {
        ProgressReport report = service.createProgressReport(student, lesson, "Treść");

        assertTrue(service.deleteProgressReport(report));
        assertTrue(service.listByStudent(student).isEmpty());
    }

    @Test
    void listByStudentReturnsOnlyMatchingReports() {
        Student other = new Student("Ewa", "Lis");
        service.createProgressReport(student, lesson, "A");
        service.createProgressReport(student, lesson, "B");
        service.createProgressReport(other, lesson, "C");

        assertEquals(2, service.listByStudent(student).size());
        assertEquals(1, service.listByStudent(other).size());
    }
}
