package scuola.users;

import scuola.attendance.Attendance;
import scuola.attendance.ProgressReport;
import scuola.lessons.Reservation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Uczeń. Składa rezerwacje, posiada statusy obecności oraz notatki o postępach.
 */
public class Student extends User {

    private String parentPhoneNumber;
    private boolean smsReminderEnabled;

    private final List<Reservation> reservations = new ArrayList<>();
    private final List<Attendance> attendanceRecords = new ArrayList<>();
    private final List<ProgressReport> progressReports = new ArrayList<>();

    public Student() {
    }

    public Student(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public Student(String firstName, String lastName, ContactInfo contactInfo) {
        super(firstName, lastName, contactInfo);
    }

    public String getParentPhoneNumber() {
        return parentPhoneNumber;
    }

    public void setParentPhoneNumber(String parentPhoneNumber) {
        this.parentPhoneNumber = parentPhoneNumber;
    }

    public boolean isSmsReminderEnabled() {
        return smsReminderEnabled;
    }

    public void setSmsReminderEnabled(boolean value) {
        this.smsReminderEnabled = value;
    }

    public void addReservation(Reservation reservation) {
        if (reservation != null) {
            reservations.add(reservation);
        }
    }

    public List<Reservation> getReservations() {
        return Collections.unmodifiableList(reservations);
    }

    public void addAttendance(Attendance attendance) {
        if (attendance != null) {
            attendanceRecords.add(attendance);
        }
    }

    public List<Attendance> getAttendanceRecords() {
        return Collections.unmodifiableList(attendanceRecords);
    }

    public void addProgressReport(ProgressReport report) {
        if (report != null) {
            progressReports.add(report);
        }
    }

    public List<ProgressReport> getProgressReports() {
        return Collections.unmodifiableList(progressReports);
    }
}
