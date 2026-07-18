package app.turnofacil.appointment;

import jakarta.validation.constraints.NotNull;

public record ChangeAppointmentStatusRequest(@NotNull AppointmentStatus status) {}
