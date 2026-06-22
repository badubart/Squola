package scuola.attendance;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AttendanceReportTest {

    @Test
    void aggregatesPresentAndAbsentCounts() {
        AttendanceReport report = new AttendanceReport(LocalDate.now().minusDays(7), LocalDate.now());
        report.addEntry(new Attendance(LocalDate.now(), true));
        report.addEntry(new Attendance(LocalDate.now(), true));
        report.addEntry(new Attendance(LocalDate.now(), false));

        assertEquals(3, report.getTotalCount());
        assertEquals(2, report.getPresentCount());
        assertEquals(1, report.getAbsentCount());
    }

    @Test
    void attendanceRateIsComputed() {
        AttendanceReport report = new AttendanceReport(LocalDate.now().minusDays(1), LocalDate.now());
        report.addEntry(new Attendance(LocalDate.now(), true));
        report.addEntry(new Attendance(LocalDate.now(), false));

        assertEquals(0.5, report.getAttendanceRate());
    }

    @Test
    void emptyReportHasZeroRate() {
        AttendanceReport report = new AttendanceReport(LocalDate.now(), LocalDate.now());
        assertEquals(0.0, report.getAttendanceRate());
    }

    @Test
    void entriesListIsReadOnly() {
        AttendanceReport report = new AttendanceReport(LocalDate.now(), LocalDate.now());
        assertThrows(UnsupportedOperationException.class,
                () -> report.getEntries().add(new Attendance(LocalDate.now(), true)));
    }
}
