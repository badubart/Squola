package scuola.repository;

import scuola.attendance.Attendance;
import scuola.lessons.Lesson;
import scuola.users.Student;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Repozytorium wpisów obecności. Źródło danych dla {@code ReportService}.
 */
public class AttendanceRepository extends InMemoryRepository<Attendance> {

    public List<Attendance> findByStudent(Student student) {
        return items.stream()
                .filter(attendance -> Objects.equals(attendance.getStudent(), student))
                .collect(Collectors.toList());
    }

    public List<Attendance> findByLesson(Lesson lesson) {
        return items.stream()
                .filter(attendance -> Objects.equals(attendance.getLesson(), lesson))
                .collect(Collectors.toList());
    }

    public List<Attendance> findBetween(LocalDate start, LocalDate end) {
        return items.stream()
                .filter(attendance -> attendance.getDate() != null)
                .filter(attendance -> {
                    LocalDate date = attendance.getDate();
                    return !date.isBefore(start) && !date.isAfter(end);
                })
                .collect(Collectors.toList());
    }
}
