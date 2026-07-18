package app.turnofacil.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ServiceOfferingResponse(
        UUID id,
        String name,
        String description,
        ServiceCategory category,
        int durationMinutes,
        BigDecimal price,
        String color,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
    static ServiceOfferingResponse from(ServiceOffering service) {
        return new ServiceOfferingResponse(service.getId(), service.getName(), service.getDescription(),
                service.getCategory(), service.getDurationMinutes(), service.getPrice(), service.getColor(),
                service.isActive(), service.getCreatedAt(), service.getUpdatedAt());
    }
}
