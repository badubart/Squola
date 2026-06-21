package scuola.repository;

import scuola.lessons.Lesson;
import scuola.lessons.Reservation;
import scuola.users.Student;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Repozytorium rezerwacji.
 */
public class ReservationRepository extends InMemoryRepository<Reservation> {

    public List<Reservation> findByStudent(Student student) {
        return items.stream()
                .filter(reservation -> Objects.equals(reservation.getStudent(), student))
                .collect(Collectors.toList());
    }

    public List<Reservation> findByLesson(Lesson lesson) {
        return items.stream()
                .filter(reservation -> Objects.equals(reservation.getLesson(), lesson))
                .collect(Collectors.toList());
    }
}
