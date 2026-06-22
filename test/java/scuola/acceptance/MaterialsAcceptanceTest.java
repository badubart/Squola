package scuola.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import scuola.attendance.Material;
import scuola.attendance.MaterialService;
import scuola.attendance.UploadedFile;
import scuola.fixtures.TestFixtures;
import scuola.lessons.Lesson;
import scuola.users.Student;
import scuola.users.Teacher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Scenariusz akceptacyjny: cykl życia materiałów dydaktycznych")
class MaterialsAcceptanceTest {

    private MaterialService materialService;

    @BeforeEach
    void setUp() {
        materialService = new MaterialService();
    }

    @Test
    @DisplayName("Nauczyciel dodaje materiał, zapisani uczniowie są powiadamiani, plik można pobrać i usunąć")
    void fullMaterialLifecycle() {
        // Given: zajęcia z dwoma zapisanymi uczniami
        Teacher teacher = TestFixtures.teacher();
        Lesson lesson = TestFixtures.lesson(teacher);
        Student first = TestFixtures.student("Jan", "Kowalski");
        Student second = TestFixtures.student("Ewa", "Lis");
        TestFixtures.enrol(lesson, first);
        TestFixtures.enrol(lesson, second);

        // When: nauczyciel wgrywa poprawny materiał
        boolean uploaded = materialService.uploadMaterial(lesson, TestFixtures.file("wyklad-1.pdf"));

        // Then: materiał dostępny, a zapisani uczniowie powiadomieni
        assertTrue(uploaded);
        assertEquals(1, materialService.listMaterials(lesson).size());
        assertEquals(1, first.getNotifications().size());
        assertEquals(1, second.getNotifications().size());

        // And: materiał można pobrać
        Material material = materialService.listMaterials(lesson).get(0);
        UploadedFile downloaded = materialService.downloadMaterial(material);
        assertEquals("wyklad-1.pdf", downloaded.getFileName());

        // And: materiał można usunąć
        assertTrue(materialService.deleteMaterial(lesson, TestFixtures.file("wyklad-1.pdf")));
        assertTrue(materialService.listMaterials(lesson).isEmpty());
    }

    @Test
    @DisplayName("Materiał przekraczający dozwolony rozmiar (100 MB) jest odrzucany")
    void oversizedMaterialIsRejected() {
        Lesson lesson = TestFixtures.lesson();

        boolean uploaded = materialService.uploadMaterial(lesson,
                TestFixtures.file("ogromny.zip", MaterialService.MAX_FILE_SIZE_MB + 1));

        assertFalse(uploaded);
        assertTrue(materialService.listMaterials(lesson).isEmpty());
    }
}
