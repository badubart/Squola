package scuola.notifications;

import scuola.users.User;

import java.time.LocalDateTime;

/**
 * Pojedyncze powiadomienie wysłane do użytkownika.
 */
public class Notification {

    private NotificationType type;
    private String content;
    private LocalDateTime sentAt;
    private User recipient;

    public Notification() {
    }

    public Notification(NotificationType type, String content) {
        this(type, content, LocalDateTime.now());
    }

    public Notification(NotificationType type, String content, LocalDateTime sentAt) {
        this(type, content, sentAt, null);
    }

    public Notification(NotificationType type, String content, LocalDateTime sentAt, User recipient) {
        this.type = type;
        this.content = content;
        this.sentAt = sentAt;
        this.recipient = recipient;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    @Override
    public String toString() {
        return "[" + type + "] " + content;
    }
}
