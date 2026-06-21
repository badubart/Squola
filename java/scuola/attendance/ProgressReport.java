package scuola.attendance;

import scuola.lessons.Lesson;
import scuola.users.Student;
import scuola.users.Teacher;

import java.time.LocalDate;

/**
 * Notatka o postępach ucznia tworzona przez nauczyciela.
 */
public class ProgressReport {

    private String content;
    private LocalDate date;
    private Student student;
    private Teacher author;
    private Lesson lesson;

    public ProgressReport() {
    }

    public ProgressReport(String content, LocalDate date) {
        this.content = content;
        this.date = date;
    }

    public ProgressReport(String content, LocalDate date, Student student, Teacher author, Lesson lesson) {
        this(content, date);
        this.student = student;
        this.author = author;
        this.lesson = lesson;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Teacher getAuthor() {
        return author;
    }

    public void setAuthor(Teacher author) {
        this.author = author;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }
}
