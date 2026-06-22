package scuola.notifications;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NotificationTypeTest {

    @Test
    void definesSmsAndEmailChannels() {
        assertEquals(2, NotificationType.values().length);
        assertNotNull(NotificationType.valueOf("SMS"));
        assertNotNull(NotificationType.valueOf("EMAIL"));
    }
}
