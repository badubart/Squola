package scuola.lessons;

import java.math.BigDecimal;

/**
 * Serwis edycji szczegółów i cen zajęć (używany przez administratora).
 */
public class LessonService {

    public boolean updateLessonDetails(Lesson lesson, String subject, String difficultyLevel) {
        if (lesson == null) {
            return false;
        }
        if (subject != null) {
            lesson.setSubject(subject);
        }
        lesson.setDifficultyLevel(difficultyLevel);
        return true;
    }

    public boolean setPrice(Lesson lesson, BigDecimal price, BigDecimal discountPercent) {
        if (lesson == null || price == null || discountPercent == null) {
            return false;
        }
        if (price.signum() < 0) {
            return false;
        }
        if (discountPercent.compareTo(BigDecimal.ZERO) < 0
                || discountPercent.compareTo(BigDecimal.valueOf(100)) > 0) {
            return false;
        }
        lesson.setPrice(price);
        lesson.setDiscountPercent(discountPercent);
        return true;
    }
}
