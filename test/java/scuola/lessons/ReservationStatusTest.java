package scuola.lessons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ReservationStatusTest {

    @Test
    void definesExpectedStatuses() {
        assertEquals(3, ReservationStatus.values().length);
        assertNotNull(ReservationStatus.valueOf("PENDING"));
        assertNotNull(ReservationStatus.valueOf("CONFIRMED"));
        assertNotNull(ReservationStatus.valueOf("CANCELLED"));
    }
}
