package app.turnofacil.auth;

import app.turnofacil.user.UserRole;

import java.time.Instant;
import java.util.UUID;

public record LoginResponse(
        String accessToken,
        String tokenType,
        Instant expiresAt,
        UserSummary user
) {
    public record UserSummary(UUID id, String name, String email, UserRole role) {}
}
