package scuola.attendance;

import scuola.repository.AttendanceRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Serwis generowania i eksportu raportów obecności. Dane obecności pobiera
 * z {@link AttendanceRepository} (warstwa danych).
 */
public class ReportService {

    private final AttendanceRepository attendanceRepository;

    public ReportService() {
        this(new AttendanceRepository());
    }

    public ReportService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public void register(Attendance attendance) {
        attendanceRepository.save(attendance);
    }

    public AttendanceReport generateAttendanceReport(LocalDate start, LocalDate end) {
        if (start == null || end == null || end.isBefore(start)) {
            throw new IllegalArgumentException("Nieprawidłowy zakres dat raportu.");
        }
        AttendanceReport report = new AttendanceReport(start, end, LocalDateTime.now());
        for (Attendance attendance : attendanceRepository.findBetween(start, end)) {
            report.addEntry(attendance);
        }
        return report;
    }

    public UploadedFile exportReport(AttendanceReport report, String format) {
        if (report == null || format == null || format.isBlank()) {
            throw new IllegalArgumentException("Nieprawidłowe argumenty eksportu.");
        }
        String extension = format.toLowerCase();
        String fileName = "raport-obecnosci." + extension;
        String content = "Okres: " + report.getPeriodStart() + " - " + report.getPeriodEnd()
                + System.lineSeparator() + "Obecności: " + report.getPresentCount()
                + System.lineSeparator() + "Nieobecności: " + report.getAbsentCount()
                + System.lineSeparator() + "Frekwencja: "
                + String.format("%.1f%%", report.getAttendanceRate() * 100);
        double sizeMB = Math.max(0.001, content.getBytes().length / (1024.0 * 1024.0));
        return new UploadedFile(fileName, sizeMB, content);
    }
}
