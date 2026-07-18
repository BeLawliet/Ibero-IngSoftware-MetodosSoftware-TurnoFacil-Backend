package app.turnofacil.client;

import jakarta.validation.constraints.NotNull;

public record ChangeClientStatusRequest(@NotNull Boolean active) {}
