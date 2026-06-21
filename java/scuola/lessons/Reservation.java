package scuola.lessons;

import scuola.users.Student;

import java.time.LocalDateTime;

/**
 * Rezerwacja zajęć złożona przez ucznia.
 */
public class Reservation {

    private Student student;
    private Lesson lesson;
    private LocalDateTime reservationDate;
    private ReservationStatus status = ReservationStatus.PENDING;

    public Reservation() {
    }

    public Reservation(Student student, Lesson lesson) {
        this(student, lesson, LocalDateTime.now());
    }

    public Reservation(Student student, Lesson lesson, LocalDateTime reservationDate) {
        this.student = student;
        this.lesson = lesson;
        this.reservationDate = reservationDate;
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

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public void confirm() {
        this.status = ReservationStatus.CONFIRMED;
    }

    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
    }

    public boolean isConfirmed() {
        return status == ReservationStatus.CONFIRMED;
    }

    public boolean isActive() {
        return status == ReservationStatus.PENDING || status == ReservationStatus.CONFIRMED;
    }
}
