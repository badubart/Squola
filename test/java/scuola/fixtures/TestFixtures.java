package scuola.fixtures;

import scuola.attendance.Attendance;
import scuola.attendance.UploadedFile;
import scuola.lessons.Lesson;
import scuola.lessons.Reservation;
import scuola.lessons.Room;
import scuola.users.Administrator;
import scuola.users.ContactInfo;
import scuola.users.Manager;
import scuola.users.Student;
import scuola.users.Teacher;
import scuola.users.TeacherAvailability;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Fixture z gotowymi danymi testowymi (wzorzec <em>Object Mother</em>).
 * Centralizuje tworzenie obiektów domenowych z sensownymi wartościami
 * domyślnymi, dzięki czemu testy są krótsze i czytelniejsze.
 */
public final class TestFixtures {

    public static final double DEFAULT_FILE_SIZE_MB = 4.0;

    private TestFixtures() {
    }

    // --- Użytkownicy ---

    public static Student student() {
        return student("Jan", "Kowalski");
    }

    public static Student student(String firstName, String lastName) {
        return new Student(firstName, lastName,
                new ContactInfo(firstName.toLowerCase() + "@student.pl", "600100200"));
    }

    public static Student studentWithSms(String firstName, String lastName) {
        Student student = student(firstName, lastName);
        student.setSmsReminderEnabled(true);
        student.setParentPhoneNumber("600999888");
        return student;
    }

    public static Teacher teacher() {
        return new Teacher("Anna", "Nowak", new ContactInfo("anna.nowak@scuola.pl", "600300400"));
    }

    public static Manager manager() {
        return new Manager("Maria", "Wiśniewska", new ContactInfo("maria@scuola.pl", "600500600"));
    }

    public static Administrator administrator() {
        return new Administrator("Adam", "Admin", new ContactInfo("adam@scuola.pl", "600700800"));
    }

    public static ContactInfo contact() {
        return new ContactInfo("kontakt@scuola.pl", "600000000");
    }

    // --- Dostępność nauczyciela ---

    public static TeacherAvailability availability(DayOfWeek day, int fromHour, int toHour) {
        return new TeacherAvailability(day, LocalTime.of(fromHour, 0), LocalTime.of(toHour, 0));
    }

    // --- Zajęcia, sale i rezerwacje ---

    public static Lesson lesson() {
        return lesson(teacher());
    }

    public static Lesson lesson(Teacher teacher) {
        LocalDateTime start = LocalDateTime.now().plusDays(2)
                .withHour(10).withMinute(0).withSecond(0).withNano(0);
        Lesson lesson = new Lesson("Matematyka", start, start.plusHours(1), teacher, 10);
        lesson.setPrice(BigDecimal.valueOf(100));
        lesson.setDiscountPercent(BigDecimal.ZERO);
        lesson.setDifficultyLevel("ŚREDNI");
        return lesson;
    }

    public static Room room() {
        return new Room("Sala 101", 20, "Projektor");
    }

    /** Zapisuje ucznia na zajęcia poprzez potwierdzoną rezerwację. */
    public static Reservation enrol(Lesson lesson, Student student) {
        Reservation reservation = new Reservation(student, lesson);
        reservation.confirm();
        lesson.addReservation(reservation);
        student.addReservation(reservation);
        return reservation;
    }

    // --- Obecności i pliki ---

    public static Attendance attendance(LocalDate date, boolean present, Student student, Lesson lesson) {
        return new Attendance(date, present, student, lesson);
    }

    public static UploadedFile file(String name) {
        return file(name, DEFAULT_FILE_SIZE_MB);
    }

    public static UploadedFile file(String name, double sizeMB) {
        return new UploadedFile(name, sizeMB, "Treść pliku " + name);
    }
}
