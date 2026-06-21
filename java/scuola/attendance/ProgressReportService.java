package scuola.attendance;

import scuola.lessons.Lesson;
import scuola.users.Student;
import scuola.users.Teacher;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Serwis zarządzania notatkami o postępach ucznia (CRUD + historia per uczeń).
 */
public class ProgressReportService {

    private final List<ProgressReport> store = new ArrayList<>();

    public ProgressReport createProgressReport(Student student, Lesson lesson, String content) {
        if (student == null || lesson == null) {
            throw new IllegalArgumentException("Uczeń i zajęcia są wymagane.");
        }
        if (!validateNotEmpty(content)) {
            throw new IllegalArgumentException("Treść notatki nie może być pusta.");
        }
        Teacher author = lesson.getTeacher();
        ProgressReport report = new ProgressReport(content, LocalDate.now(), student, author, lesson);
        store.add(report);
        student.addProgressReport(report);
        if (author != null) {
            author.addCreatedProgressReport(report);
        }
        return report;
    }

    public boolean updateProgressReport(ProgressReport report, String content) {
        if (report == null || !validateNotEmpty(content) || !store.contains(report)) {
            return false;
        }
        report.setContent(content);
        return true;
    }

    public boolean deleteProgressReport(ProgressReport report) {
        return report != null && store.remove(report);
    }

    public List<ProgressReport> listByStudent(Student student) {
        List<ProgressReport> result = new ArrayList<>();
        for (ProgressReport report : store) {
            if (Objects.equals(report.getStudent(), student)) {
                result.add(report);
            }
        }
        return result;
    }

    private boolean validateNotEmpty(String content) {
        return content != null && !content.isBlank();
    }
}
