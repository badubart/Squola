package scuola.repository;

import scuola.users.Student;
import scuola.users.Teacher;
import scuola.users.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Repozytorium użytkowników. Pozwala odpytywać o pracowników (kadrę)
 * oraz nauczycieli.
 */
public class UserRepository extends InMemoryRepository<User> {

    public List<User> findEmployees() {
        return items.stream()
                .filter(user -> !(user instanceof Student))
                .collect(Collectors.toList());
    }

    public List<Teacher> findTeachers() {
        return items.stream()
                .filter(user -> user instanceof Teacher)
                .map(user -> (Teacher) user)
                .collect(Collectors.toList());
    }
}
