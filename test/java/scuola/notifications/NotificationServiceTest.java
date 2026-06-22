package scuola.notifications;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scuola.attendance.Material;
import scuola.lessons.Lesson;
import scuola.lessons.Reservation;
import scuola.users.Student;
import scuola.users.Teacher;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotificationServiceTest {

    private NotificationService service;
    private Student student;
    private Lesson lesson;

    @BeforeEach
    void setUp() {
        service = new NotificationService();
        student = new Student("Jan", "Kowalski");
        lesson = new Lesson("Matematyka", null, null);
    }

    @Test
    void reservationConfirmationReachesStudentBySms() {
        service.sendReservationConfirmation(new Reservation(student, lesson));

        assertEquals(1, student.getNotifications().size());
        Notification notification = student.getNotifications().get(0);
        assertEquals(NotificationType.SMS, notification.getType());
        assertTrue(notification.getContent().contains("Matematyka"));
        assertSame(student, notification.getRecipient());
    }

    @Test
    void lessonReminderUsesSmsWhenEnabled() {
        student.setSmsReminderEnabled(true);

        service.sendLessonReminder(new Reservation(student, lesson));

        assertEquals(NotificationType.SMS, student.getNotifications().get(0).getType());
    }

    @Test
    void lessonReminderIsSuppressedWhenSmsDisabled() {
        service.sendLessonReminder(new Reservation(student, lesson));

        assertTrue(student.getNotifications().isEmpty());
    }

    @Test
    void cancelNoticeNotifiesTeacherAndEnrolledStudents() {
        Teacher teacher = new Teacher("Anna", "Nowak");
        lesson.setTeacher(teacher);
        enrol(student);

        service.sendCancelNotice(lesson);

        assertEquals(1, teacher.getNotifications().size());
        assertEquals(1, student.getNotifications().size());
        assertTrue(student.getNotifications().get(0).getContent().contains("Odwołano"));
    }

    @Test
    void materialNoticeNotifiesEnrolledStudents() {
        enrol(student);
        Material material = new Material("notatki.pdf", 1.0);
        material.setLesson(lesson);

        service.sendMaterialNotice(material);

        assertEquals(1, student.getNotifications().size());
        assertTrue(student.getNotifications().get(0).getContent().contains("notatki.pdf"));
    }

    @Test
    void notifyAbsencesReachesStudent() {
        service.notifyAbsences(student);

        assertEquals(1, student.getNotifications().size());
        assertTrue(student.getNotifications().get(0).getContent().contains("nieobecności"));
    }

    @Test
    void nullArgumentsAreIgnoredSafely() {
        assertDoesNotThrow(() -> {
            service.sendReservationConfirmation(null);
            service.sendCancelNotice(null);
            service.sendMaterialNotice(null);
            service.notifyAbsences(null);
            service.sendLessonReminder(null);
        });
    }

    private void enrol(Student enrolled) {
        Reservation reservation = new Reservation(enrolled, lesson);
        reservation.confirm();
        lesson.addReservation(reservation);
    }
}
