package scuola.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AvailabilityServiceTest {

    private AvailabilityService service;
    private Teacher teacher;

    @BeforeEach
    void setUp() {
        service = new AvailabilityService();
        teacher = new Teacher("Anna", "Nowak");
    }

    @Test
    void updateAvailabilityAcceptsNonConflictingSlots() {
        boolean result = service.updateAvailability(teacher, List.of(
                new TeacherAvailability(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(10, 0)),
                new TeacherAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0))));

        assertTrue(result);
        assertEquals(2, teacher.getAvailabilities().size());
    }

    @Test
    void updateAvailabilityRejectsOverlappingSlots() {
        boolean result = service.updateAvailability(teacher, List.of(
                new TeacherAvailability(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(11, 0)),
                new TeacherAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0))));

        assertFalse(result);
        assertTrue(teacher.getAvailabilities().isEmpty());
    }

    @Test
    void updateAvailabilityRejectsInvalidSlot() {
        boolean result = service.updateAvailability(teacher, List.of(
                new TeacherAvailability(DayOfWeek.MONDAY, LocalTime.of(12, 0), LocalTime.of(10, 0))));
        assertFalse(result);
    }

    @Test
    void updateAvailabilityRejectsNullArguments() {
        assertFalse(service.updateAvailability(null, List.of()));
        assertFalse(service.updateAvailability(teacher, null));
    }

    @Test
    void submitThrowsOnConflict() {
        assertThrows(IllegalArgumentException.class, () -> service.submit(teacher, List.of(
                new TeacherAvailability(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(11, 0)),
                new TeacherAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0)))));
    }

    @Test
    void submitSucceedsForValidSlots() {
        service.submit(teacher, List.of(
                new TeacherAvailability(DayOfWeek.FRIDAY, LocalTime.of(8, 0), LocalTime.of(9, 0))));
        assertEquals(1, teacher.getAvailabilities().size());
    }

    @Test
    void listAvailabilityReturnsTeacherSlots() {
        service.updateAvailability(teacher, List.of(
                new TeacherAvailability(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(10, 0)),
                new TeacherAvailability(DayOfWeek.TUESDAY, LocalTime.of(8, 0), LocalTime.of(10, 0))));

        assertEquals(2, service.listAvailability(teacher).size());
    }

    @Test
    void listAllAvailabilityAggregatesAcrossTeachers() {
        Teacher other = new Teacher("Piotr", "Zięba");
        service.updateAvailability(teacher, List.of(
                new TeacherAvailability(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(10, 0)),
                new TeacherAvailability(DayOfWeek.TUESDAY, LocalTime.of(8, 0), LocalTime.of(10, 0))));
        service.updateAvailability(other, List.of(
                new TeacherAvailability(DayOfWeek.FRIDAY, LocalTime.of(12, 0), LocalTime.of(14, 0))));

        assertEquals(3, service.listAllAvailability().size());
    }
}
