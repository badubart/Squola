package scuola.attendance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReportServiceTest {

    private ReportService service;

    @BeforeEach
    void setUp() {
        service = new ReportService();
    }

    @Test
    void generateAggregatesAttendancesWithinRange() {
        service.register(new Attendance(LocalDate.of(2026, 6, 10), true));
        service.register(new Attendance(LocalDate.of(2026, 6, 12), false));
        service.register(new Attendance(LocalDate.of(2026, 1, 1), true)); // poza zakresem

        AttendanceReport report = service.generateAttendanceReport(
                LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30));

        assertEquals(2, report.getTotalCount());
        assertEquals(1, report.getPresentCount());
        assertEquals(1, report.getAbsentCount());
    }

    @Test
    void generateIncludesBoundaryDates() {
        service.register(new Attendance(LocalDate.of(2026, 6, 1), true));
        service.register(new Attendance(LocalDate.of(2026, 6, 30), true));

        AttendanceReport report = service.generateAttendanceReport(
                LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30));

        assertEquals(2, report.getTotalCount());
    }

    @Test
    void generateRejectsInvalidRange() {
        assertThrows(IllegalArgumentException.class, () -> service.generateAttendanceReport(
                LocalDate.of(2026, 6, 30), LocalDate.of(2026, 6, 1)));
        assertThrows(IllegalArgumentException.class,
                () -> service.generateAttendanceReport(null, LocalDate.now()));
    }

    @Test
    void exportProducesFileWithRequestedExtension() {
        AttendanceReport report = service.generateAttendanceReport(
                LocalDate.now().minusDays(1), LocalDate.now());

        UploadedFile file = service.exportReport(report, "csv");

        assertTrue(file.getFileName().endsWith(".csv"));
        assertTrue(file.getContent().contains("Frekwencja"));
    }

    @Test
    void exportRejectsInvalidArguments() {
        AttendanceReport report = service.generateAttendanceReport(
                LocalDate.now().minusDays(1), LocalDate.now());

        assertThrows(IllegalArgumentException.class, () -> service.exportReport(null, "csv"));
        assertThrows(IllegalArgumentException.class, () -> service.exportReport(report, " "));
    }
}
