package scuola.users;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Serwis obsługujący deklaracje dostępności nauczycieli (zapis i odczyt).
 */
public class AvailabilityService {

    private final Set<Teacher> managedTeachers = new LinkedHashSet<>();


    public boolean updateAvailability(Teacher teacher, List<TeacherAvailability> slots) {
        if (teacher == null || slots == null || hasConflict(teacher, slots)) {
            return false;
        }
        teacher.setAvailabilities(slots);
        managedTeachers.add(teacher);
        return true;
    }

    public void submit(Teacher teacher, List<TeacherAvailability> slots) {
        if (!updateAvailability(teacher, slots)) {
            throw new IllegalArgumentException("Nieprawidłowe lub kolidujące terminy dostępności.");
        }
    }

    public List<TeacherAvailability> listAvailability(Teacher teacher) {
        return teacher == null ? List.of() : new ArrayList<>(teacher.getAvailabilities());
    }

    public List<TeacherAvailability> listAllAvailability() {
        List<TeacherAvailability> all = new ArrayList<>();
        for (Teacher teacher : managedTeachers) {
            all.addAll(teacher.getAvailabilities());
        }
        return all;
    }

    private boolean hasConflict(Teacher teacher, List<TeacherAvailability> slots) {
        for (TeacherAvailability slot : slots) {
            if (slot == null || !slot.isValid()) {
                return true;
            }
        }
        for (int i = 0; i < slots.size(); i++) {
            for (int j = i + 1; j < slots.size(); j++) {
                if (slots.get(i).overlaps(slots.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }
}
