package app.turnofacil.appointment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record UpdateAppointmentRequest(
        @NotNull UUID clientId,
        @NotNull UUID employeeId,
        @NotNull UUID serviceId,
        @NotNull LocalDate appointmentDate,
        @NotNull LocalTime startTime,
        @Size(max = 2000) String notes
) {}
