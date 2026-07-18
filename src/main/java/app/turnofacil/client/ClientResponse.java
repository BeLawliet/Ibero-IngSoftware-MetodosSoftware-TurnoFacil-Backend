package app.turnofacil.client;

import java.time.Instant;
import java.util.UUID;

public record ClientResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone,
        boolean active,
        String notes,
        Instant createdAt,
        Instant updatedAt
) {
    static ClientResponse from(Client client) {
        return new ClientResponse(client.getId(), client.getFirstName(), client.getLastName(),
                client.getEmail(), client.getPhone(), client.isActive(), client.getNotes(),
                client.getCreatedAt(), client.getUpdatedAt());
    }
}
