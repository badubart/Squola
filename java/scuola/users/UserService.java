package scuola.users;

import scuola.repository.UserRepository;

import java.util.List;

/**
 * Serwis zarządzania kadrą (pracownikami). Zapytania realizuje przez
 * {@link UserRepository}.
 */
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createEmployee(String firstName, String lastName, ContactInfo contact) {
        return createEmployee(firstName, lastName, contact, EmployeeRole.TEACHER);
    }

    public User createEmployee(String firstName, String lastName, ContactInfo contact, EmployeeRole role) {
        User user = switch (role) {
            case TEACHER -> new Teacher(firstName, lastName, contact);
            case ADMINISTRATOR -> new Administrator(firstName, lastName, contact);
            case MANAGER -> new Manager(firstName, lastName, contact);
        };
        userRepository.save(user);
        return user;
    }

    public List<User> listEmployees() {
        return userRepository.findEmployees();
    }

    public boolean updateEmployee(User user) {
        if (user == null) {
            return false;
        }
        userRepository.save(user);
        return true;
    }

    public boolean deactivateEmployee(User user) {
        if (user == null) {
            return false;
        }
        user.setActive(false);
        return true;
    }
}
