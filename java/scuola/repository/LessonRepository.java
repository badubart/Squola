package scuola.repository;

import scuola.lessons.Lesson;
import scuola.lessons.LessonStatus;
import scuola.users.Student;
import scuola.users.Teacher;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Repozytorium zajęć. Obsługuje zapytania używane przez
 * {@code ReservationService} i {@code ScheduleService}.
 */
public class LessonRepository extends InMemoryRepository<Lesson> {

    public List<Lesson> findByTeacher(Teacher teacher) {
        return items.stream()
                .filter(lesson -> Objects.equals(lesson.getTeacher(), teacher))
                .collect(Collectors.toList());
    }

    public List<Lesson> findByStudent(Student student) {
        return items.stream()
                .filter(lesson -> lesson.getEnrolledStudents().contains(student))
                .collect(Collectors.toList());
    }

    public List<Lesson> findScheduled() {
        return items.stream()
                .filter(lesson -> lesson.getStatus() == LessonStatus.SCHEDULED)
                .collect(Collectors.toList());
    }

    public List<Lesson> findStartingBetween(LocalDate start, LocalDate end) {
        return items.stream()
                .filter(lesson -> lesson.getStartTime() != null)
                .filter(lesson -> {
                    LocalDate date = lesson.getStartTime().toLocalDate();
                    return !date.isBefore(start) && !date.isAfter(end);
                })
                .collect(Collectors.toList());
    }
}
