package scuola.users;

import scuola.notifications.Notification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Bazowy, abstrakcyjny użytkownik systemu.
 */
public abstract class User {

    private String firstName;
    private String lastName;
    private ContactInfo contactInfo;
    private boolean active = true;
    private final List<Notification> notifications = new ArrayList<>();

    protected User() {
    }

    protected User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    protected User(String firstName, String lastName, ContactInfo contactInfo) {
        this(firstName, lastName);
        this.contactInfo = contactInfo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getFullName() {
        String first = firstName != null ? firstName : "";
        String last = lastName != null ? lastName : "";
        return (first + " " + last).trim();
    }

    public void addNotification(Notification notification) {
        if (notification != null) {
            notifications.add(notification);
        }
    }

    public List<Notification> getNotifications() {
        return Collections.unmodifiableList(notifications);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
