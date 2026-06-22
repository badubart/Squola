package scuola.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import scuola.fixtures.TestFixtures;
import scuola.lessons.Lesson;
import scuola.lessons.Reservation;
import scuola.users.AvailabilityService;
import scuola.users.Teacher;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Scenariusz akceptacyjny: deklaracja dostępności nauczyciela")
class AvailabilityAcceptanceTest {

    private final AvailabilityService service = new AvailabilityService();

    @Test
    @DisplayName("Nauczyciel zgłasza niekolidujące terminy dostępności")
    void teacherSubmitsNonConflictingAvailability() {
        // Given: nauczyciel i dwa rozłączne terminy
        Teacher teacher = TestFixtures.teacher();

        // When: nauczyciel zatwierdza dostępność
        service.submit(teacher, List.of(
                TestFixtures.availability(DayOfWeek.MONDAY, 8, 12),
                TestFixtures.availability(DayOfWeek.WEDNESDAY, 14, 16)));

        // Then: oba terminy zostają zapisane
        assertEquals(2, teacher.getAvailabilities().size());
    }

    @Test
    @DisplayName("Kolidujące terminy są odrzucane i dostępność pozostaje pusta")
    void conflictingAvailabilityIsRejected() {
        // Given: nauczyciel i dwa nakładające się terminy w tym samym dniu
        Teacher teacher = TestFixtures.teacher();

        // When / Then: zatwierdzenie kończy się wyjątkiem
        assertThrows(IllegalArgumentException.class, () -> service.submit(teacher, List.of(
                TestFixtures.availability(DayOfWeek.MONDAY, 8, 11),
                TestFixtures.availability(DayOfWeek.MONDAY, 10, 12))));

        // And: dostępność nie została zapisana
        assertTrue(teacher.getAvailabilities().isEmpty());
    }

    @Test
    @DisplayName("Usunięcie terminu z aktywną rezerwacją jest odrzucane (wymaga zatwierdzenia admina)")
    void removingSlotWithActiveReservationIsRejected() {
        // Given: nauczyciel z zajęciami w czwartek 16:00 i aktywną rezerwacją ucznia
        Teacher teacher = TestFixtures.teacher();
        LocalDateTime thursday = LocalDateTime.of(2026, 6, 25, 16, 0); // czwartek
        Lesson lesson = new Lesson("Matematyka", thursday, thursday.plusHours(1), teacher, 5);
        Reservation reservation = new Reservation(TestFixtures.student(), lesson);
        reservation.confirm();
        lesson.addReservation(reservation);
        teacher.addLesson(lesson);

        // When: nauczyciel ustawia dostępność, która NIE obejmuje czwartku 16:00
        boolean result = service.updateAvailability(teacher, List.of(
                TestFixtures.availability(DayOfWeek.MONDAY, 8, 12)));

        // Then: zmiana zostaje odrzucona
        assertFalse(result);
    }

    @Test
    @DisplayName("Dostępność obejmująca zarezerwowane zajęcia jest akceptowana")
    void slotCoveringBookedLessonIsAccepted() {
        Teacher teacher = TestFixtures.teacher();
        LocalDateTime thursday = LocalDateTime.of(2026, 6, 25, 16, 0); // czwartek
        Lesson lesson = new Lesson("Matematyka", thursday, thursday.plusHours(1), teacher, 5);
        Reservation reservation = new Reservation(TestFixtures.student(), lesson);
        reservation.confirm();
        lesson.addReservation(reservation);
        teacher.addLesson(lesson);

        // When: dostępność obejmuje czwartek 15:00-18:00
        boolean result = service.updateAvailability(teacher, List.of(
                TestFixtures.availability(DayOfWeek.THURSDAY, 15, 18)));

        // Then: zmiana zostaje przyjęta
        assertTrue(result);
    }
}
