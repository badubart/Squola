package scuola.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import scuola.attendance.Attendance;
import scuola.attendance.AttendanceReport;
import scuola.attendance.AttendanceService;
import scuola.attendance.ReportService;
import scuola.attendance.UploadedFile;
import scuola.fixtures.TestFixtures;
import scuola.lessons.Lesson;
import scuola.users.Manager;
import scuola.users.Student;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Scenariusz akceptacyjny: generowanie i eksport raportu obecności")
class ReportingAcceptanceTest {

    private final AttendanceService attendanceService = new AttendanceService();
    private final ReportService reportService = new ReportService();

    @Test
    @DisplayName("Kierownik generuje raport obecności za okres i eksportuje go do CSV")
    void managerGeneratesAndExportsReport() {
        // Given: zajęcia z obecnościami dwojga uczniów
        Lesson lesson = TestFixtures.lesson();
        Student first = TestFixtures.student("Jan", "Kowalski");
        Student second = TestFixtures.student("Ewa", "Lis");
        attendanceService.markAttendance(lesson, first, true);
        attendanceService.markAttendance(lesson, first, false);
        attendanceService.markAttendance(lesson, second, true);
        lesson.getAttendances().forEach(reportService::register);

        // When: kierownik generuje raport za bieżący okres
        Manager manager = TestFixtures.manager();
        AttendanceReport report = reportService.generateAttendanceReport(
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        manager.addGeneratedReport(report);

        // Then: raport agreguje wszystkie obecności z okresu
        assertEquals(3, report.getTotalCount());
        assertEquals(2, report.getPresentCount());
        assertEquals(1, report.getAbsentCount());
        assertEquals(1, manager.getGeneratedReports().size());

        // And: eksport tworzy plik CSV z podsumowaniem frekwencji
        UploadedFile exported = reportService.exportReport(report, "csv");
        assertTrue(exported.getFileName().endsWith(".csv"));
        assertTrue(exported.getContent().contains("Frekwencja"));
    }

    @Test
    @DisplayName("Raport pomija obecności spoza wybranego okresu")
    void reportExcludesAttendancesOutsideRange() {
        Lesson lesson = TestFixtures.lesson();
        Student student = TestFixtures.student();
        reportService.register(new Attendance(LocalDate.now(), true, student, lesson));
        reportService.register(new Attendance(LocalDate.now().minusMonths(2), false, student, lesson));

        AttendanceReport report = reportService.generateAttendanceReport(
                LocalDate.now().minusDays(7), LocalDate.now().plusDays(1));

        assertEquals(1, report.getTotalCount());
    }
}
