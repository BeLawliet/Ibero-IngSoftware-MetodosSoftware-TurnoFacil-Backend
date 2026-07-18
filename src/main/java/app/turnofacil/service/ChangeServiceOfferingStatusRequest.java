package app.turnofacil.service;

import jakarta.validation.constraints.NotNull;

public record ChangeServiceOfferingStatusRequest(@NotNull Boolean active) {}
