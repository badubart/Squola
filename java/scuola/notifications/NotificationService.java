package scuola.notifications;

import scuola.attendance.Material;
import scuola.lessons.Lesson;
import scuola.lessons.Reservation;
import scuola.users.Student;
import scuola.users.User;

import java.time.LocalDateTime;

/**
 * Serwis powiadomień. Zgodnie z założeniami projektu wysyłka jest uproszczona
 * do wypisania komunikatu w konsoli; dodatkowo każde powiadomienie jest
 * dopinane do listy powiadomień odbiorcy (recipient), co pozwala je testować.
 */
public class NotificationService {

    public void sendReservationConfirmation(Reservation reservation) {
        if (reservation == null || reservation.getStudent() == null) {
            return;
        }
        String content = "Potwierdzenie rezerwacji: " + lessonName(reservation.getLesson());
        deliver(reservation.getStudent(), content, NotificationType.EMAIL);
    }

    public void sendCancelNotice(Lesson lesson) {
        if (lesson == null) {
            return;
        }
        String content = "Odwołano zajęcia: " + lessonName(lesson);
        if (lesson.getTeacher() != null) {
            deliver(lesson.getTeacher(), content, NotificationType.EMAIL);
        }
        for (Student student : lesson.getEnrolledStudents()) {
            deliver(student, content, channelFor(student));
        }
    }

    public void sendMaterialNotice(Material material) {
        if (material == null || material.getLesson() == null) {
            return;
        }
        String content = "Nowy materiał: " + material.getFileName();
        for (Student student : material.getLesson().getEnrolledStudents()) {
            deliver(student, content, channelFor(student));
        }
    }

    public void notifyAbsences(Student student) {
        if (student == null) {
            return;
        }
        deliver(student, "Uwaga: przekroczono dozwoloną liczbę nieobecności.", channelFor(student));
    }

    public void sendLessonReminder(Reservation reservation) {
        if (reservation == null || reservation.getStudent() == null) {
            return;
        }
        String content = "Przypomnienie o zajęciach: " + lessonName(reservation.getLesson());
        deliver(reservation.getStudent(), content, channelFor(reservation.getStudent()));
    }

    private NotificationType channelFor(Student student) {
        return student.isSmsReminderEnabled() ? NotificationType.SMS : NotificationType.EMAIL;
    }

    private String lessonName(Lesson lesson) {
        return lesson != null && lesson.getSubject() != null ? lesson.getSubject() : "zajęcia";
    }

    private void deliver(User user, String content, NotificationType type) {
        Notification notification = new Notification(type, content, LocalDateTime.now(), user);
        user.addNotification(notification);
        System.out.println("[POWIADOMIENIE][" + type + "] -> " + user.getFullName() + ": " + content);
    }
}
