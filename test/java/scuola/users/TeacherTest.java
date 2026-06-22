package scuola.users;

import org.junit.jupiter.api.Test;
import scuola.attendance.ProgressReport;
import scuola.lessons.Lesson;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TeacherTest {

    @Test
    void lessonsAreCollected() {
        Teacher teacher = new Teacher("Anna", "Nowak");
        teacher.addLesson(new Lesson("Matematyka", null, null));
        assertEquals(1, teacher.getLessons().size());
    }

    @Test
    void setAvailabilitiesReplacesPreviousSlots() {
        Teacher teacher = new Teacher("Anna", "Nowak");
        teacher.addAvailability(new TeacherAvailability(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(9, 0)));

        teacher.setAvailabilities(List.of(
                new TeacherAvailability(DayOfWeek.TUESDAY, LocalTime.of(10, 0), LocalTime.of(11, 0))));

        assertEquals(1, teacher.getAvailabilities().size());
        assertEquals(DayOfWeek.TUESDAY, teacher.getAvailabilities().get(0).getDayOfWeek());
    }

    @Test
    void createdProgressReportsAreCollected() {
        Teacher teacher = new Teacher("Anna", "Nowak");
        teacher.addCreatedProgressReport(new ProgressReport("Notatka", LocalDate.now()));
        assertEquals(1, teacher.getCreatedProgressReports().size());
    }

    @Test
    void availabilitiesListIsReadOnly() {
        Teacher teacher = new Teacher("Anna", "Nowak");
        assertThrows(UnsupportedOperationException.class,
                () -> teacher.getAvailabilities().add(
                        new TeacherAvailability(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(9, 0))));
    }
}
