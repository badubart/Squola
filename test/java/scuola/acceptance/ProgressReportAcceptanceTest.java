package scuola.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import scuola.attendance.ProgressReport;
import scuola.attendance.ProgressReportService;
import scuola.fixtures.TestFixtures;
import scuola.lessons.Lesson;
import scuola.users.Student;
import scuola.users.Teacher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Realizacja historyjki US 3.4 - notatki o postępach ucznia (CRUD + historia).
 */
@DisplayName("Scenariusz akceptacyjny: notatki o postępach ucznia (US 3.4)")
class ProgressReportAcceptanceTest {

    private final ProgressReportService service = new ProgressReportService();

    @Test
    @DisplayName("Nauczyciel prowadzi pełną historię notatek ucznia (utworzenie, edycja, usunięcie)")
    void fullProgressReportLifecycle() {
        // Given: zajęcia prowadzone przez nauczyciela oraz uczeń
        Teacher teacher = TestFixtures.teacher();
        Lesson lesson = TestFixtures.lesson(teacher);
        Student student = TestFixtures.student();

        // When: nauczyciel tworzy dwie notatki
        ProgressReport first = service.createProgressReport(student, lesson, "Dobre podstawy");
        ProgressReport second = service.createProgressReport(student, lesson, "Wymaga ćwiczeń z ułamków");

        // Then: obie notatki trafiają do historii ucznia, autorem jest nauczyciel
        assertEquals(2, service.listByStudent(student).size());
        assertSame(teacher, first.getAuthor());
        assertEquals(2, teacher.getCreatedProgressReports().size());

        // When: nauczyciel aktualizuje pierwszą notatkę
        assertTrue(service.updateProgressReport(first, "Bardzo dobre podstawy"));
        assertEquals("Bardzo dobre podstawy", first.getContent());

        // When: nauczyciel usuwa drugą notatkę
        assertTrue(service.deleteProgressReport(second));

        // Then: w historii ucznia pozostaje jedna notatka
        assertEquals(1, service.listByStudent(student).size());
    }

    @Test
    @DisplayName("Notatka z pustą treścią nie zostaje zapisana")
    void emptyNoteIsRejected() {
        Lesson lesson = TestFixtures.lesson();
        Student student = TestFixtures.student();

        assertThrows(IllegalArgumentException.class,
                () -> service.createProgressReport(student, lesson, "   "));
        assertTrue(service.listByStudent(student).isEmpty());
    }
}
