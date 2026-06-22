package scuola.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scuola.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserServiceTest {

    private UserRepository repository;
    private UserService service;

    @BeforeEach
    void setUp() {
        repository = new UserRepository();
        service = new UserService(repository);
    }

    @Test
    void createEmployeeDefaultsToTeacher() {
        User user = service.createEmployee("Anna", "Nowak", new ContactInfo("a@x.pl", "1"));

        assertInstanceOf(Teacher.class, user);
        assertEquals(1, service.listEmployees().size());
    }

    @Test
    void createEmployeeRespectsRole() {
        assertInstanceOf(Manager.class,
                service.createEmployee("M", "M", null, EmployeeRole.MANAGER));
        assertInstanceOf(Administrator.class,
                service.createEmployee("A", "A", null, EmployeeRole.ADMINISTRATOR));
    }

    @Test
    void listEmployeesExcludesStudents() {
        repository.save(new Student("Jan", "Kowalski"));
        service.createEmployee("Anna", "Nowak", null);

        assertEquals(1, service.listEmployees().size());
    }

    @Test
    void deactivateEmployeeSetsInactive() {
        User user = service.createEmployee("Anna", "Nowak", null);

        assertTrue(service.deactivateEmployee(user));
        assertFalse(user.isActive());
    }

    @Test
    void updateEmployeeStoresUser() {
        Teacher teacher = new Teacher("Anna", "Nowak");

        assertTrue(service.updateEmployee(teacher));
        assertEquals(1, service.listEmployees().size());
    }

    @Test
    void nullArgumentsAreRejected() {
        assertFalse(service.deactivateEmployee(null));
        assertFalse(service.updateEmployee(null));
    }
}
