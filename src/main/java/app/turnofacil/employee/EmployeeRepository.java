package app.turnofacil.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    boolean existsByEmail(String normalizedEmail);
    boolean existsByEmailAndIdNot(String normalizedEmail, UUID id);
    List<Employee> findAllByOrderByLastNameAscFirstNameAsc();
}
