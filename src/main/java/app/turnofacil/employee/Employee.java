package app.turnofacil.employee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    private UUID id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 254)
    private String email;

    @Column(nullable = false, length = 30)
    private String phone;

    @Column(nullable = false, length = 120)
    private String specialty;

    @Column(nullable = false, length = 7)
    private String color;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EmployeeAvailability availability;

    @Column(nullable = false)
    private boolean active;

    @Column(length = 2000)
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Employee() {
    }

    private Employee(String firstName, String lastName, String email, String phone,
                     String specialty, String color, String notes) {
        this.id = UUID.randomUUID();
        updateContactInformation(firstName, lastName, email, phone, specialty, color, notes);
        this.active = true;
        this.availability = EmployeeAvailability.AVAILABLE;
    }

    public static Employee create(String firstName, String lastName, String email, String phone,
                                  String specialty, String color, String notes) {
        return new Employee(firstName, lastName, email, phone, specialty, color, notes);
    }

    public void updateContactInformation(String firstName, String lastName, String email,
                                         String phone, String specialty, String color, String notes) {
        this.firstName = normalizeText(firstName);
        this.lastName = normalizeText(lastName);
        this.email = normalizeEmail(email);
        this.phone = normalizeText(phone);
        this.specialty = normalizeText(specialty);
        this.color = color == null ? null : color.trim().toUpperCase(Locale.ROOT);
        this.notes = normalizeOptionalText(notes);
    }

    public void changeActiveStatus(boolean active) {
        this.active = active;
        this.availability = active ? EmployeeAvailability.AVAILABLE : EmployeeAvailability.OFF_SHIFT;
    }

    public void changeAvailability(EmployeeAvailability availability) {
        if (!active && availability != EmployeeAvailability.OFF_SHIFT) {
            throw new IllegalStateException("Un empleado inactivo debe permanecer fuera de turno");
        }
        this.availability = availability;
    }

    public static String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
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
        if (!active) availability = EmployeeAvailability.OFF_SHIFT;
        updatedAt = Instant.now();
    }

    public UUID getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getSpecialty() { return specialty; }
    public String getColor() { return color; }
    public EmployeeAvailability getAvailability() { return availability; }
    public boolean isActive() { return active; }
    public String getNotes() { return notes; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
