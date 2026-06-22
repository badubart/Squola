package scuola.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scuola.lessons.Lesson;
import scuola.lessons.LessonStatus;
import scuola.lessons.Reservation;
import scuola.users.Student;
import scuola.users.Teacher;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class LessonRepositoryTest {

    private LessonRepository repository;

    @BeforeEach
    void setUp() {
        repository = new LessonRepository();
    }

    @Test
    void findByTeacherReturnsOnlyTheirLessons() {
        Teacher teacher = new Teacher("Anna", "Nowak");
        Lesson own = new Lesson("Matematyka", null, null, teacher);
        repository.save(own);
        repository.save(new Lesson("Fizyka", null, null, new Teacher("Piotr", "Zięba")));

        assertEquals(1, repository.findByTeacher(teacher).size());
        assertSame(own, repository.findByTeacher(teacher).get(0));
    }

    @Test
    void findScheduledExcludesCancelledLessons() {
        repository.save(new Lesson("Matematyka", null, null));
        Lesson cancelled = new Lesson("Fizyka", null, null);
        cancelled.setStatus(LessonStatus.CANCELLED);
        repository.save(cancelled);

        assertEquals(1, repository.findScheduled().size());
    }

    @Test
    void findStartingBetweenFiltersByDate() {
        Lesson today = new Lesson("Matematyka", LocalDate.now().atTime(10, 0), null);
        Lesson nextWeek = new Lesson("Fizyka", LocalDate.now().plusDays(7).atTime(10, 0), null);
        repository.save(today);
        repository.save(nextWeek);

        assertEquals(1, repository.findStartingBetween(LocalDate.now(), LocalDate.now()).size());
    }

    @Test
    void findByStudentReturnsLessonsWithReservation() {
        Student student = new Student("Jan", "Kowalski");
        Lesson lesson = new Lesson("Matematyka", null, null);
        Reservation reservation = new Reservation(student, lesson);
        reservation.confirm();
        lesson.addReservation(reservation);
        repository.save(lesson);
        repository.save(new Lesson("Fizyka", null, null));

        assertEquals(1, repository.findByStudent(student).size());
    }
}
