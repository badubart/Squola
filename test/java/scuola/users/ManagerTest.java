package scuola.users;

import org.junit.jupiter.api.Test;
import scuola.attendance.AttendanceReport;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ManagerTest {

    @Test
    void generatedReportsAreCollected() {
        Manager manager = new Manager("Maria", "Wiśniewska");
        manager.addGeneratedReport(new AttendanceReport(LocalDate.now(), LocalDate.now()));

        assertEquals(1, manager.getGeneratedReports().size());
    }

    @Test
    void generatedReportsListIsReadOnly() {
        Manager manager = new Manager("Maria", "Wiśniewska");
        assertThrows(UnsupportedOperationException.class,
                () -> manager.getGeneratedReports().add(new AttendanceReport(LocalDate.now(), LocalDate.now())));
    }

    @Test
    void inheritsFullName() {
        Manager manager = new Manager("Maria", "Wiśniewska");
        assertEquals("Maria Wiśniewska", manager.getFullName());
    }
}
