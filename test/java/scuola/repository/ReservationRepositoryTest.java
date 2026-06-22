package scuola.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scuola.lessons.Lesson;
import scuola.lessons.Reservation;
import scuola.users.Student;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReservationRepositoryTest {

    private ReservationRepository repository;
    private Student student;
    private Lesson lesson;

    @BeforeEach
    void setUp() {
        repository = new ReservationRepository();
        student = new Student("Jan", "Kowalski");
        lesson = new Lesson("Matematyka", null, null);
    }

    @Test
    void findByStudentReturnsMatchingReservations() {
        repository.save(new Reservation(student, lesson));
        repository.save(new Reservation(new Student("Ewa", "Lis"), lesson));

        assertEquals(1, repository.findByStudent(student).size());
    }

    @Test
    void findByLessonReturnsMatchingReservations() {
        repository.save(new Reservation(student, lesson));
        repository.save(new Reservation(student, new Lesson("Fizyka", null, null)));

        assertEquals(1, repository.findByLesson(lesson).size());
    }
}
