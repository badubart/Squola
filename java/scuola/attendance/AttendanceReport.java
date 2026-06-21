package scuola.attendance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AttendanceReport {

    private LocalDate periodStart;
    private LocalDate periodEnd;
    private LocalDateTime generatedAt;
    private final List<Attendance> entries = new ArrayList<>();

    public AttendanceReport() {
    }

    public AttendanceReport(LocalDate periodStart, LocalDate periodEnd) {
        this(periodStart, periodEnd, LocalDateTime.now());
    }

    public AttendanceReport(LocalDate periodStart, LocalDate periodEnd, LocalDateTime generatedAt) {
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.generatedAt = generatedAt;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public void addEntry(Attendance attendance) {
        if (attendance != null) {
            entries.add(attendance);
        }
    }

    public List<Attendance> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    public int getTotalCount() {
        return entries.size();
    }

    public long getPresentCount() {
        return entries.stream().filter(Attendance::isPresent).count();
    }

    public long getAbsentCount() {
        return entries.stream().filter(a -> !a.isPresent()).count();
    }

    public double getAttendanceRate() {
        if (entries.isEmpty()) {
            return 0.0;
        }
        return (double) getPresentCount() / getTotalCount();
    }
}
