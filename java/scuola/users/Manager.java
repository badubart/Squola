package scuola.users;

import scuola.attendance.AttendanceReport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Kierownik. Generuje raporty obecności.
 */
public class Manager extends User {

    private final List<AttendanceReport> generatedReports = new ArrayList<>();

    public Manager() {
    }

    public Manager(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public Manager(String firstName, String lastName, ContactInfo contactInfo) {
        super(firstName, lastName, contactInfo);
    }

    public void addGeneratedReport(AttendanceReport report) {
        if (report != null) {
            generatedReports.add(report);
        }
    }

    public List<AttendanceReport> getGeneratedReports() {
        return Collections.unmodifiableList(generatedReports);
    }
}
