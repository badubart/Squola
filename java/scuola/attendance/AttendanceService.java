package scuola.attendance;

import scuola.lessons.Lesson;
import scuola.notifications.NotificationService;
import scuola.repository.AttendanceRepository;
import scuola.users.Student;

import java.time.LocalDate;
import java.util.List;

/**
 * Serwis oznaczania obecności i kontroli progu nieobecności.
 * Po przekroczeniu progu automatycznie powiadamia ucznia.
 */
public class AttendanceService {

    private int absenceLimit = 3;

    private final AttendanceRepository attendanceRepository;
    private final NotificationService notificationService;

    public AttendanceService() {
        this(new AttendanceRepository(), new NotificationService());
    }

    public AttendanceService(AttendanceRepository attendanceRepository, NotificationService notificationService) {
        this.attendanceRepository = attendanceRepository;
        this.notificationService = notificationService;
    }

    public boolean markAttendance(Lesson lesson, Student student, boolean isPresent) {
        if (lesson == null || student == null) {
            return false;
        }
        Attendance attendance = new Attendance(LocalDate.now(), isPresent, student, lesson);
        lesson.addAttendance(attendance);
        student.addAttendance(attendance);
        attendanceRepository.save(attendance);
        if (!isPresent && checkTooManyAbsences(student)) {
            notificationService.notifyAbsences(student);
        }
        return true;
    }

    public boolean updateAttendance(Attendance attendance, boolean isPresent) {
        if (attendance == null) {
            return false;
        }
        attendance.setPresent(isPresent);
        return true;
    }

    public List<Student> listStudentsForLesson(Lesson lesson) {
        return lesson == null ? List.of() : lesson.getEnrolledStudents();
    }

    public void setAbsenceLimit(int limit) {
        this.absenceLimit = limit;
    }

    public int getAbsenceLimit() {
        return absenceLimit;
    }

    public boolean checkTooManyAbsences(Student student) {
        if (student == null) {
            return false;
        }
        long absences = student.getAttendanceRecords().stream()
                .filter(attendance -> !attendance.isPresent())
                .count();
        return absences > absenceLimit;
    }

    public AttendanceRepository getAttendanceRepository() {
        return attendanceRepository;
    }
}
