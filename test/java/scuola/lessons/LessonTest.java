package scuola.lessons;

import org.junit.jupiter.api.Test;
import scuola.attendance.Attendance;
import scuola.attendance.Material;
import scuola.users.Student;
import scuola.users.Teacher;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LessonTest {

    @Test
    void constructorStoresFields() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);
        Teacher teacher = new Teacher("Anna", "Nowak");

        Lesson lesson = new Lesson("Matematyka", start, end, teacher);

        assertEquals("Matematyka", lesson.getSubject());
        assertEquals(start, lesson.getStartTime());
        assertEquals(end, lesson.getEndTime());
        assertSame(teacher, lesson.getTeacher());
    }

    @Test
    void defaultStatusIsScheduled() {
        assertEquals(LessonStatus.SCHEDULED, new Lesson().getStatus());
    }

    @Test
    void finalPriceAppliesDiscount() {
        Lesson lesson = new Lesson("Matematyka", null, null);
        lesson.setPrice(new BigDecimal("200"));
        lesson.setDiscountPercent(new BigDecimal("10"));

        assertEquals(new BigDecimal("180.00"), lesson.getFinalPrice());
    }

    @Test
    void availableSeatsCountActiveReservations() {
        Lesson lesson = new Lesson("Matematyka", null, null);
        lesson.setMaxSeats(2);
        Reservation reservation = new Reservation(new Student("Jan", "Kowalski"), lesson);
        reservation.confirm();
        lesson.addReservation(reservation);

        assertEquals(1, lesson.getAvailableSeats());

        reservation.cancel();
        assertEquals(2, lesson.getAvailableSeats());
    }

    @Test
    void enrolledStudentsComeFromActiveReservations() {
        Lesson lesson = new Lesson("Matematyka", null, null);
        lesson.setMaxSeats(5);
        Student student = new Student("Jan", "Kowalski");
        Reservation reservation = new Reservation(student, lesson);
        reservation.confirm();
        lesson.addReservation(reservation);

        assertEquals(1, lesson.getEnrolledStudents().size());
        assertTrue(lesson.getEnrolledStudents().contains(student));

        reservation.cancel();
        assertTrue(lesson.getEnrolledStudents().isEmpty());
    }

    @Test
    void materialsCanBeAddedAndRemoved() {
        Lesson lesson = new Lesson("Matematyka", null, null);
        Material material = new Material("plik.pdf", 1.0);

        lesson.addMaterial(material);
        assertEquals(1, lesson.getMaterials().size());

        assertTrue(lesson.removeMaterial(material));
        assertEquals(0, lesson.getMaterials().size());
    }

    @Test
    void removeMaterialReturnsFalseWhenAbsent() {
        Lesson lesson = new Lesson("Matematyka", null, null);
        assertFalse(lesson.removeMaterial(new Material("inny.pdf", 1.0)));
    }

    @Test
    void attendancesCanBeAdded() {
        Lesson lesson = new Lesson("Matematyka", null, null);
        lesson.addAttendance(new Attendance(LocalDate.now(), true));
        assertEquals(1, lesson.getAttendances().size());
    }

    @Test
    void materialsListIsReadOnly() {
        Lesson lesson = new Lesson("Matematyka", null, null);
        assertThrows(UnsupportedOperationException.class,
                () -> lesson.getMaterials().add(new Material("x.pdf", 1.0)));
    }
}
