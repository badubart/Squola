package scuola.attendance;

import scuola.lessons.Lesson;
import scuola.users.Student;

import java.time.LocalDate;

public class Attendance {

    private LocalDate date;
    private boolean isPresent;
    private Student student;
    private Lesson lesson;

    public Attendance() {
    }

    public Attendance(LocalDate date, boolean isPresent) {
        this.date = date;
        this.isPresent = isPresent;
    }

    public Attendance(LocalDate date, boolean isPresent, Student student, Lesson lesson) {
        this(date, isPresent);
        this.student = student;
        this.lesson = lesson;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        this.isPresent = present;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }
}
