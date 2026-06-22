package scuola.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scuola.attendance.Attendance;
import scuola.lessons.Lesson;
import scuola.users.Student;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AttendanceRepositoryTest {

    private AttendanceRepository repository;
    private Student student;
    private Lesson lesson;

    @BeforeEach
    void setUp() {
        repository = new AttendanceRepository();
        student = new Student("Jan", "Kowalski");
        lesson = new Lesson("Matematyka", null, null);
    }

    @Test
    void findByStudentReturnsMatchingEntries() {
        repository.save(new Attendance(LocalDate.now(), true, student, lesson));
        repository.save(new Attendance(LocalDate.now(), false, new Student("Ewa", "Lis"), lesson));

        assertEquals(1, repository.findByStudent(student).size());
    }

    @Test
    void findByLessonReturnsMatchingEntries() {
        repository.save(new Attendance(LocalDate.now(), true, student, lesson));
        repository.save(new Attendance(LocalDate.now(), true, student, new Lesson("Fizyka", null, null)));

        assertEquals(1, repository.findByLesson(lesson).size());
    }

    @Test
    void findBetweenFiltersByDate() {
        repository.save(new Attendance(LocalDate.of(2026, 6, 10), true, student, lesson));
        repository.save(new Attendance(LocalDate.of(2026, 1, 1), false, student, lesson));

        assertEquals(1, repository.findBetween(LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30)).size());
    }
}
