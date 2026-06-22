package scuola.attendance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scuola.lessons.Lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MaterialServiceTest {

    private MaterialService service;
    private Lesson lesson;

    @BeforeEach
    void setUp() {
        service = new MaterialService();
        lesson = new Lesson("Matematyka", null, null);
    }

    @Test
    void uploadValidFileAddsMaterial() {
        boolean result = service.uploadMaterial(lesson, new UploadedFile("plik.pdf", 5.0));

        assertTrue(result);
        assertEquals(1, lesson.getMaterials().size());
        assertEquals("plik.pdf", lesson.getMaterials().get(0).getFileName());
    }

    @Test
    void uploadRejectsTooLargeFile() {
        boolean result = service.uploadMaterial(lesson, new UploadedFile("duzy.zip", 150.0));

        assertFalse(result);
        assertTrue(lesson.getMaterials().isEmpty());
    }

    @Test
    void uploadRejectsNonPositiveSize() {
        assertFalse(service.uploadMaterial(lesson, new UploadedFile("pusty.pdf", 0.0)));
    }

    @Test
    void uploadRejectsNullArguments() {
        assertFalse(service.uploadMaterial(null, new UploadedFile("plik.pdf", 1.0)));
        assertFalse(service.uploadMaterial(lesson, null));
    }

    @Test
    void deleteRemovesMatchingMaterial() {
        service.uploadMaterial(lesson, new UploadedFile("plik.pdf", 5.0));

        assertTrue(service.deleteMaterial(lesson, new UploadedFile("plik.pdf", 5.0)));
        assertTrue(lesson.getMaterials().isEmpty());
    }

    @Test
    void deleteReturnsFalseWhenMaterialMissing() {
        assertFalse(service.deleteMaterial(lesson, new UploadedFile("nieistnieje.pdf", 1.0)));
    }

    @Test
    void listMaterialsReturnsEmptyForNullLesson() {
        assertTrue(service.listMaterials(null).isEmpty());
    }

    @Test
    void downloadReturnsFileWithSameName() {
        Material material = new Material("plik.pdf", 5.0);
        UploadedFile downloaded = service.downloadMaterial(material);

        assertEquals("plik.pdf", downloaded.getFileName());
        assertEquals(5.0, downloaded.getSizeMB());
    }

    @Test
    void downloadReturnsNullForNullMaterial() {
        assertNull(service.downloadMaterial(null));
    }
}
