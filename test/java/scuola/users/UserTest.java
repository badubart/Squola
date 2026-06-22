package scuola.users;

import org.junit.jupiter.api.Test;
import scuola.notifications.Notification;
import scuola.notifications.NotificationType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    /** Minimalna konkretna implementacja do testowania klasy abstrakcyjnej. */
    private static class TestUser extends User {
        TestUser(String firstName, String lastName) {
            super(firstName, lastName);
        }
    }

    @Test
    void fullNameCombinesFirstAndLast() {
        User user = new TestUser("Jan", "Kowalski");
        assertEquals("Jan Kowalski", user.getFullName());
    }

    @Test
    void fullNameTrimsWhenLastNameMissing() {
        User user = new TestUser("Jan", null);
        assertEquals("Jan", user.getFullName());
    }

    @Test
    void contactInfoCanBeAssigned() {
        User user = new TestUser("Jan", "Kowalski");
        ContactInfo contact = new ContactInfo("jan@x.pl", "600");
        user.setContactInfo(contact);
        assertSame(contact, user.getContactInfo());
    }

    @Test
    void notificationsAreStoredAndExposedAsReadOnly() {
        User user = new TestUser("Jan", "Kowalski");
        user.addNotification(new Notification(NotificationType.EMAIL, "x"));

        assertEquals(1, user.getNotifications().size());
        assertThrows(UnsupportedOperationException.class,
                () -> user.getNotifications().add(new Notification(NotificationType.SMS, "y")));
    }

    @Test
    void addNotificationIgnoresNull() {
        User user = new TestUser("Jan", "Kowalski");
        user.addNotification(null);
        assertEquals(0, user.getNotifications().size());
    }

    @Test
    void userIsActiveByDefaultAndCanBeDeactivated() {
        User user = new TestUser("Jan", "Kowalski");
        assertTrue(user.isActive());

        user.setActive(false);
        assertFalse(user.isActive());
    }
}
