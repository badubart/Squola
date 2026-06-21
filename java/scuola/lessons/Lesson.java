package scuola.lessons;

import scuola.attendance.Attendance;
import scuola.attendance.Material;
import scuola.users.Student;
import scuola.users.Teacher;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Pojedyncze zajęcia prowadzone przez nauczyciela. Zawierają materiały,
 * statusy obecności oraz rezerwacje uczniów; mogą odbywać się w sali (Room).
 */
public class Lesson {

    private String subject;
    private String difficultyLevel;
    private BigDecimal price = BigDecimal.ZERO;
    private BigDecimal discountPercent = BigDecimal.ZERO;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int maxSeats;
    private LessonStatus status = LessonStatus.SCHEDULED;
    private Teacher teacher;
    private Room room;

    private final List<Material> materials = new ArrayList<>();
    private final List<Attendance> attendances = new ArrayList<>();
    private final List<Reservation> reservations = new ArrayList<>();

    public Lesson() {
    }

    public Lesson(String subject, LocalDateTime startTime, LocalDateTime endTime) {
        this.subject = subject;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Lesson(String subject, LocalDateTime startTime, LocalDateTime endTime, Teacher teacher) {
        this(subject, startTime, endTime);
        this.teacher = teacher;
    }

    public Lesson(String subject, LocalDateTime startTime, LocalDateTime endTime, Teacher teacher, int maxSeats) {
        this(subject, startTime, endTime, teacher);
        this.maxSeats = maxSeats;
    }

    // --- metody obliczeniowe z diagramu klas ---

    public BigDecimal getFinalPrice() {
        BigDecimal base = price != null ? price : BigDecimal.ZERO;
        BigDecimal discount = discountPercent != null ? discountPercent : BigDecimal.ZERO;
        BigDecimal factor = BigDecimal.ONE.subtract(discount.divide(BigDecimal.valueOf(100)));
        return base.multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }

    public int getAvailableSeats() {
        long taken = reservations.stream().filter(Reservation::isActive).count();
        return maxSeats - (int) taken;
    }

    public List<Student> getEnrolledStudents() {
        Set<Student> students = new LinkedHashSet<>();
        for (Reservation reservation : reservations) {
            if (reservation.isActive() && reservation.getStudent() != null) {
                students.add(reservation.getStudent());
            }
        }
        return new ArrayList<>(students);
    }

    // --- rezerwacje / materiały / obecności ---

    public void addReservation(Reservation reservation) {
        if (reservation != null) {
            reservations.add(reservation);
        }
    }

    public List<Reservation> getReservations() {
        return Collections.unmodifiableList(reservations);
    }

    public void addMaterial(Material material) {
        if (material != null) {
            materials.add(material);
        }
    }

    public boolean removeMaterial(Material material) {
        return materials.remove(material);
    }

    public List<Material> getMaterials() {
        return Collections.unmodifiableList(materials);
    }

    public void addAttendance(Attendance attendance) {
        if (attendance != null) {
            attendances.add(attendance);
        }
    }

    public List<Attendance> getAttendances() {
        return Collections.unmodifiableList(attendances);
    }

    // --- gettery i settery ---

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getMaxSeats() {
        return maxSeats;
    }

    public void setMaxSeats(int maxSeats) {
        this.maxSeats = maxSeats;
    }

    public LessonStatus getStatus() {
        return status;
    }

    public void setStatus(LessonStatus status) {
        this.status = status;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
