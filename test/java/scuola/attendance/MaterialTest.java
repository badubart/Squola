package scuola.attendance;

import org.junit.jupiter.api.Test;
import scuola.lessons.Lesson;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class MaterialTest {

    @Test
    void constructorStoresAllFields() {
        LocalDateTime uploaded = LocalDateTime.now();
        Lesson lesson = new Lesson("Matematyka", null, null);

        Material material = new Material("wyklad.pdf", 4.2, uploaded, lesson);

        assertEquals("wyklad.pdf", material.getFileName());
        assertEquals(4.2, material.getFileSizeMB());
        assertEquals(uploaded, material.getUploadDate());
        assertSame(lesson, material.getLesson());
    }

    @Test
    void shortConstructorSetsUploadDate() {
        Material material = new Material("plik.pdf", 1.0);
        assertEquals("plik.pdf", material.getFileName());
        assertEquals(1.0, material.getFileSizeMB());
    }
}
