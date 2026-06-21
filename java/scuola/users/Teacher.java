package scuola.users;

import scuola.attendance.ProgressReport;
import scuola.lessons.Lesson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Nauczyciel. Prowadzi zajęcia, deklaruje dostępność i tworzy notatki o postępach.
 */
public class Teacher extends User {

    private final List<Lesson> lessons = new ArrayList<>();
    private final List<TeacherAvailability> availabilities = new ArrayList<>();
    private final List<ProgressReport> createdProgressReports = new ArrayList<>();

    public Teacher() {
    }

    public Teacher(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public Teacher(String firstName, String lastName, ContactInfo contactInfo) {
        super(firstName, lastName, contactInfo);
    }

    public void addLesson(Lesson lesson) {
        if (lesson != null) {
            lessons.add(lesson);
        }
    }

    public List<Lesson> getLessons() {
        return Collections.unmodifiableList(lessons);
    }

    public List<TeacherAvailability> getAvailabilities() {
        return Collections.unmodifiableList(availabilities);
    }

    public void setAvailabilities(List<TeacherAvailability> slots) {
        availabilities.clear();
        if (slots != null) {
            availabilities.addAll(slots);
        }
    }

    public void addAvailability(TeacherAvailability slot) {
        if (slot != null) {
            availabilities.add(slot);
        }
    }

    public void addCreatedProgressReport(ProgressReport report) {
        if (report != null) {
            createdProgressReports.add(report);
        }
    }

    public List<ProgressReport> getCreatedProgressReports() {
        return Collections.unmodifiableList(createdProgressReports);
    }
}
