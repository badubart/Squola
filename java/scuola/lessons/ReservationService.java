package scuola.lessons;

import scuola.attendance.UploadedFile;
import scuola.notifications.NotificationService;
import scuola.repository.LessonRepository;
import scuola.repository.ReservationRepository;
import scuola.users.Student;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serwis rezerwacji i przeglądania harmonogramu zajęć.
 * Zapytania o zajęcia/rezerwacje realizuje przez repozytoria (warstwa danych).
 */
public class ReservationService {

    private final LessonRepository lessonRepository;
    private final ReservationRepository reservationRepository;
    private final NotificationService notificationService;

    public ReservationService(LessonRepository lessonRepository,
                              ReservationRepository reservationRepository,
                              NotificationService notificationService) {
        this.lessonRepository = lessonRepository;
        this.reservationRepository = reservationRepository;
        this.notificationService = notificationService;
    }

    public List<Lesson> getAvailableLessons(String filter) {
        return lessonRepository.findScheduled().stream()
                .filter(lesson -> lesson.getAvailableSeats() > 0)
                .filter(lesson -> matchesFilter(lesson, filter))
                .collect(Collectors.toList());
    }

    public boolean checkAvailability(Lesson lesson) {
        return lesson != null && lesson.getAvailableSeats() > 0;
    }

    public boolean createReservation(Student student, Lesson lesson) {
        if (student == null || !checkAvailability(lesson)) {
            return false;
        }
        Reservation reservation = persistReservation(student, lesson);
        notificationService.sendReservationConfirmation(reservation);
        return true;
    }

    public boolean cancelReservation(Reservation reservation) {
        if (reservation == null) {
            return false;
        }
        Lesson lesson = reservation.getLesson();
        if (!canCancel(lesson)) {
            return false;
        }
        // powiadamiamy zanim zwolnimy miejsce (uczeń jest jeszcze zapisany)
        notificationService.sendCancelNotice(lesson);
        reservation.cancel();
        return true;
    }

    public int countLessons(LocalDate start, LocalDate end) {
        if (!validateDateRange(start, end)) {
            return 0;
        }
        return lessonRepository.findStartingBetween(start, end).size();
    }

    public List<Reservation> createReservations(Student student, LocalDate start, LocalDate end) {
        if (student == null || !validateDateRange(start, end)) {
            throw new IllegalArgumentException("Niepoprawny zakres dat lub brak ucznia.");
        }
        List<Reservation> created = new ArrayList<>();
        for (Lesson lesson : lessonRepository.findStartingBetween(start, end)) {
            if (lesson.getStatus() == LessonStatus.SCHEDULED && lesson.getAvailableSeats() > 0) {
                created.add(persistReservation(student, lesson));
            }
        }
        return created;
    }

    public UploadedFile exportToCalendar(Student student) {
        List<Reservation> reservations = student == null ? List.of()
                : reservationRepository.findByStudent(student).stream()
                        .filter(Reservation::isActive)
                        .collect(Collectors.toList());

        StringBuilder calendar = new StringBuilder("BEGIN:VCALENDAR").append(System.lineSeparator());
        for (Reservation reservation : reservations) {
            Lesson lesson = reservation.getLesson();
            calendar.append("BEGIN:VEVENT").append(System.lineSeparator());
            calendar.append("SUMMARY:")
                    .append(lesson != null && lesson.getSubject() != null ? lesson.getSubject() : "Zajęcia")
                    .append(System.lineSeparator());
            if (lesson != null && lesson.getStartTime() != null) {
                calendar.append("DTSTART:").append(lesson.getStartTime()).append(System.lineSeparator());
            }
            calendar.append("END:VEVENT").append(System.lineSeparator());
        }
        calendar.append("END:VCALENDAR").append(System.lineSeparator());

        String content = calendar.toString();
        double sizeMB = Math.max(0.001, content.getBytes().length / (1024.0 * 1024.0));
        return new UploadedFile("plan-zajec.ics", sizeMB, content);
    }

    private Reservation persistReservation(Student student, Lesson lesson) {
        Reservation reservation = new Reservation(student, lesson);
        reservation.confirm();
        reservationRepository.save(reservation);
        lesson.addReservation(reservation);
        student.addReservation(reservation);
        return reservation;
    }

    private boolean canCancel(Lesson lesson) {
        if (lesson == null || lesson.getStartTime() == null) {
            return true; // brak terminu -> brak ograniczenia 24h
        }
        return lesson.getStartTime().isAfter(LocalDateTime.now().plusHours(24));
    }

    private boolean matchesFilter(Lesson lesson, String filter) {
        if (filter == null || filter.isBlank()) {
            return true;
        }
        String needle = filter.toLowerCase();
        return (lesson.getSubject() != null && lesson.getSubject().toLowerCase().contains(needle))
                || (lesson.getDifficultyLevel() != null && lesson.getDifficultyLevel().toLowerCase().contains(needle));
    }

    private boolean validateDateRange(LocalDate start, LocalDate end) {
        return start != null && end != null && !end.isBefore(start);
    }
}
