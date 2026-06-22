package scuola.lessons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LessonStatusTest {

    @Test
    void definesExpectedStatuses() {
        assertEquals(3, LessonStatus.values().length);
        assertNotNull(LessonStatus.valueOf("SCHEDULED"));
        assertNotNull(LessonStatus.valueOf("CANCELLED"));
        assertNotNull(LessonStatus.valueOf("COMPLETED"));
    }
}
