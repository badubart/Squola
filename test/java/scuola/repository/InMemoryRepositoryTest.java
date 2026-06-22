package scuola.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scuola.lessons.Room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testy bazowego repozytorium (na konkretnej implementacji {@link RoomRepository}).
 */
class InMemoryRepositoryTest {

    private RoomRepository repository;

    @BeforeEach
    void setUp() {
        repository = new RoomRepository();
    }

    @Test
    void saveAddsItemAndFindAllReturnsIt() {
        Room room = new Room("Sala 1", 10, "");
        repository.save(room);

        assertEquals(1, repository.count());
        assertTrue(repository.findAll().contains(room));
    }

    @Test
    void saveIgnoresNullAndDuplicates() {
        Room room = new Room("Sala 1", 10, "");
        repository.save(null);
        repository.save(room);
        repository.save(room);

        assertEquals(1, repository.count());
    }

    @Test
    void deleteRemovesItem() {
        Room room = new Room("Sala 1", 10, "");
        repository.save(room);

        assertTrue(repository.delete(room));
        assertEquals(0, repository.count());
    }

    @Test
    void findAllReturnsImmutableCopy() {
        repository.save(new Room("Sala 1", 10, ""));
        assertThrows(UnsupportedOperationException.class,
                () -> repository.findAll().add(new Room("Sala 2", 10, "")));
    }

    @Test
    void clearRemovesEverything() {
        repository.save(new Room("Sala 1", 10, ""));
        repository.clear();
        assertEquals(0, repository.count());
    }
}
