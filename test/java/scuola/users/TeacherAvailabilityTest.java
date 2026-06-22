package scuola.users;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TeacherAvailabilityTest {

    @Test
    void isValidWhenStartBeforeEnd() {
        TeacherAvailability slot =
                new TeacherAvailability(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(10, 0));
        assertTrue(slot.isValid());
    }

    @Test
    void isInvalidWhenStartAfterEnd() {
        TeacherAvailability slot =
                new TeacherAvailability(DayOfWeek.MONDAY, LocalTime.of(11, 0), LocalTime.of(10, 0));
        assertFalse(slot.isValid());
    }

    @Test
    void overlappingSlotsOnSameDayAreDetected() {
        TeacherAvailability a =
                new TeacherAvailability(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(10, 0));
        TeacherAvailability b =
                new TeacherAvailability(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(11, 0));
        assertTrue(a.overlaps(b));
        assertTrue(b.overlaps(a));
    }

    @Test
    void slotsOnDifferentDaysDoNotOverlap() {
        TeacherAvailability a =
                new TeacherAvailability(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(10, 0));
        TeacherAvailability b =
                new TeacherAvailability(DayOfWeek.TUESDAY, LocalTime.of(8, 0), LocalTime.of(10, 0));
        assertFalse(a.overlaps(b));
    }

    @Test
    void adjacentSlotsDoNotOverlap() {
        TeacherAvailability a =
                new TeacherAvailability(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(10, 0));
        TeacherAvailability b =
                new TeacherAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0));
        assertFalse(a.overlaps(b));
    }
}
