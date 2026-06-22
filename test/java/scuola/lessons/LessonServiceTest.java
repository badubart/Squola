package scuola.lessons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LessonServiceTest {

    private LessonService service;
    private Lesson lesson;

    @BeforeEach
    void setUp() {
        service = new LessonService();
        lesson = new Lesson("Matematyka", null, null);
    }

    @Test
    void updateLessonDetailsChangesFields() {
        boolean result = service.updateLessonDetails(lesson, "Fizyka", "ZAAWANSOWANY");

        assertTrue(result);
        assertEquals("Fizyka", lesson.getSubject());
        assertEquals("ZAAWANSOWANY", lesson.getDifficultyLevel());
    }

    @Test
    void updateLessonDetailsRejectsNullLesson() {
        assertFalse(service.updateLessonDetails(null, "Fizyka", "ŁATWY"));
    }

    @Test
    void setPriceAppliesValuesAndAffectsFinalPrice() {
        boolean result = service.setPrice(lesson, new BigDecimal("200"), new BigDecimal("10"));

        assertTrue(result);
        assertEquals(new BigDecimal("200"), lesson.getPrice());
        assertEquals(new BigDecimal("180.00"), lesson.getFinalPrice());
    }

    @Test
    void setPriceRejectsNegativePrice() {
        assertFalse(service.setPrice(lesson, new BigDecimal("-1"), BigDecimal.ZERO));
    }

    @Test
    void setPriceRejectsInvalidDiscount() {
        assertFalse(service.setPrice(lesson, new BigDecimal("100"), new BigDecimal("150")));
        assertFalse(service.setPrice(lesson, new BigDecimal("100"), new BigDecimal("-5")));
    }

    @Test
    void setPriceRejectsNullArguments() {
        assertFalse(service.setPrice(null, BigDecimal.TEN, BigDecimal.ZERO));
        assertFalse(service.setPrice(lesson, null, BigDecimal.ZERO));
        assertFalse(service.setPrice(lesson, BigDecimal.TEN, null));
    }
}
