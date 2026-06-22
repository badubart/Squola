package scuola.notifications;

import org.junit.jupiter.api.Test;
import scuola.users.Student;
import scuola.users.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class NotificationTest {

    @Test
    void fullConstructorStoresAllFields() {
        LocalDateTime sentAt = LocalDateTime.of(2026, 6, 18, 10, 0);
        User recipient = new Student("Jan", "Kowalski");
        Notification notification = new Notification(NotificationType.EMAIL, "Treść", sentAt, recipient);

        assertEquals(NotificationType.EMAIL, notification.getType());
        assertEquals("Treść", notification.getContent());
        assertEquals(sentAt, notification.getSentAt());
        assertSame(recipient, notification.getRecipient());
    }

    @Test
    void convenienceConstructorSetsSentAtAutomatically() {
        Notification notification = new Notification(NotificationType.SMS, "Treść");

        assertEquals(NotificationType.SMS, notification.getType());
        assertNotNull(notification.getSentAt());
    }

    @Test
    void settersUpdateValues() {
        Notification notification = new Notification();
        notification.setType(NotificationType.SMS);
        notification.setContent("Nowa");

        assertEquals(NotificationType.SMS, notification.getType());
        assertEquals("Nowa", notification.getContent());
    }
}
