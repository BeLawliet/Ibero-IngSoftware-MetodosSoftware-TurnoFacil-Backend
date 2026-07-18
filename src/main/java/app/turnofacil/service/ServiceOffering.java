package app.turnofacil.service;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@Entity
@Table(name = "services")
public class ServiceOffering {

    @Id
    private UUID id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ServiceCategory category;

    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, length = 7)
    private String color;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected ServiceOffering() {
    }

    private ServiceOffering(String name, String description, ServiceCategory category,
                            int durationMinutes, BigDecimal price, String color) {
        this.id = UUID.randomUUID();
        updateDetails(name, description, category, durationMinutes, price, color);
        this.active = true;
    }

    public static ServiceOffering create(String name, String description, ServiceCategory category,
                                         int durationMinutes, BigDecimal price, String color) {
        return new ServiceOffering(name, description, category, durationMinutes, price, color);
    }

    public void updateDetails(String name, String description, ServiceCategory category,
                              int durationMinutes, BigDecimal price, String color) {
        this.name = normalizeText(name);
        this.description = normalizeOptionalText(description);
        this.category = category;
        this.durationMinutes = durationMinutes;
        this.price = price;
        this.color = color == null ? null : color.trim().toUpperCase(Locale.ROOT);
    }

    public void changeActiveStatus(boolean active) {
        this.active = active;
    }

    public static String normalizeName(String name) {
        return normalizeText(name);
    }

    private static String normalizeText(String value) {
        return value == null ? null : value.trim().replaceAll("\\s+", " ");
    }

    private static String normalizeOptionalText(String value) {
        if (value == null) return null;
        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public ServiceCategory getCategory() { return category; }
    public int getDurationMinutes() { return durationMinutes; }
    public BigDecimal getPrice() { return price; }
    public String getColor() { return color; }
    public boolean isActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
