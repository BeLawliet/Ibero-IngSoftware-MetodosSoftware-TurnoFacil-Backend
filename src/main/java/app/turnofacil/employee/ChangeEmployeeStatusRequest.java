package app.turnofacil.employee;

import jakarta.validation.constraints.NotNull;

public record ChangeEmployeeStatusRequest(@NotNull Boolean active) {}
