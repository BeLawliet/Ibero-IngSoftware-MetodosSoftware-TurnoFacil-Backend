package app.turnofacil.appointment;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record AppointmentResponse(
        UUID id,
        ClientSummary client,
        EmployeeSummary employee,
        ServiceSummary service,
        LocalDate appointmentDate,
        LocalTime startTime,
        LocalTime endTime,
        int durationMinutes,
        AppointmentStatus status,
        String notes,
        Instant createdAt,
        Instant updatedAt
) {
    static AppointmentResponse from(Appointment appointment) {
        var client = appointment.getClient();
        var employee = appointment.getEmployee();
        var service = appointment.getService();
        return new AppointmentResponse(
                appointment.getId(),
                new ClientSummary(client.getId(), client.getFirstName(), client.getLastName(), client.getEmail()),
                new EmployeeSummary(employee.getId(), employee.getFirstName(), employee.getLastName(),
                        employee.getSpecialty(), employee.getColor()),
                new ServiceSummary(service.getId(), service.getName(), service.getPrice(), service.getColor()),
                appointment.getAppointmentDate(), appointment.getStartTime(), appointment.getEndTime(),
                appointment.getDurationMinutes(), appointment.getStatus(), appointment.getNotes(),
                appointment.getCreatedAt(), appointment.getUpdatedAt());
    }

    public record ClientSummary(UUID id, String firstName, String lastName, String email) {}
    public record EmployeeSummary(UUID id, String firstName, String lastName, String specialty, String color) {}
    public record ServiceSummary(UUID id, String name, BigDecimal price, String color) {}
}
