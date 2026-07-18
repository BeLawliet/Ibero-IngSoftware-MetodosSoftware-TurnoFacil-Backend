package app.turnofacil.employee;

import java.time.Instant;
import java.util.UUID;

public record EmployeeResponse(
        UUID id, String firstName, String lastName, String email, String phone,
        String specialty, String color, EmployeeAvailability availability,
        boolean active, String notes, Instant createdAt, Instant updatedAt
) {
    static EmployeeResponse from(Employee employee) {
        return new EmployeeResponse(employee.getId(), employee.getFirstName(), employee.getLastName(),
                employee.getEmail(), employee.getPhone(), employee.getSpecialty(), employee.getColor(),
                employee.getAvailability(), employee.isActive(), employee.getNotes(),
                employee.getCreatedAt(), employee.getUpdatedAt());
    }
}
