package scuola.users;

import scuola.attendance.Attendance;
import scuola.lessons.Lesson;

/**
 * Administrator. Może edytować szczegóły zajęć oraz sprawdzać i edytować
 * listy obecności.
 */
public class Administrator extends User {

    public Administrator() {
    }

    public Administrator(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public Administrator(String firstName, String lastName, ContactInfo contactInfo) {
        super(firstName, lastName, contactInfo);
    }

    public void editLessonDetails(Lesson lesson, String subject) {
        if (lesson != null) {
            lesson.setSubject(subject);
        }
    }

    public void editAttendance(Attendance attendance, boolean isPresent) {
        if (attendance != null) {
            attendance.setPresent(isPresent);
        }
    }
}
