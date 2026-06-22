package scuola.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scuola.users.Administrator;
import scuola.users.Student;
import scuola.users.Teacher;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserRepositoryTest {

    private UserRepository repository;

    @BeforeEach
    void setUp() {
        repository = new UserRepository();
    }

    @Test
    void findEmployeesExcludesStudents() {
        repository.save(new Teacher("Anna", "Nowak"));
        repository.save(new Administrator("Adam", "Admin"));
        repository.save(new Student("Jan", "Kowalski"));

        assertEquals(2, repository.findEmployees().size());
    }

    @Test
    void findTeachersReturnsOnlyTeachers() {
        repository.save(new Teacher("Anna", "Nowak"));
        repository.save(new Administrator("Adam", "Admin"));
        repository.save(new Student("Jan", "Kowalski"));

        assertEquals(1, repository.findTeachers().size());
    }
}
