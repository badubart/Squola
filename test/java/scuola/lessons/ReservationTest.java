package scuola.lessons;

import org.junit.jupiter.api.Test;
import scuola.users.Student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReservationTest {

    @Test
    void constructorLinksStudentAndLesson() {
        Student student = new Student("Jan", "Kowalski");
        Lesson lesson = new Lesson("Matematyka", null, null);

        Reservation reservation = new Reservation(student, lesson);

        assertSame(student, reservation.getStudent());
        assertSame(lesson, reservation.getLesson());
        assertNotNull(reservation.getReservationDate());
    }

    @Test
    void newReservationIsPendingAndActive() {
        Reservation reservation = new Reservation(new Student("Jan", "Kowalski"), null);

        assertEquals(ReservationStatus.PENDING, reservation.getStatus());
        assertFalse(reservation.isConfirmed());
        assertTrue(reservation.isActive());
    }

    @Test
    void confirmMarksReservationConfirmed() {
        Reservation reservation = new Reservation(new Student("Jan", "Kowalski"), null);

        reservation.confirm();

        assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());
        assertTrue(reservation.isConfirmed());
        assertTrue(reservation.isActive());
    }

    @Test
    void cancelMarksReservationCancelledAndInactive() {
        Reservation reservation = new Reservation(new Student("Jan", "Kowalski"), null);

        reservation.cancel();

        assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
        assertFalse(reservation.isActive());
    }
}
