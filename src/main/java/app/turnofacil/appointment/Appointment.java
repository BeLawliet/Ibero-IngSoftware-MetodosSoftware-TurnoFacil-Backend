package app.turnofacil.appointment;

import app.turnofacil.client.Client;
import app.turnofacil.employee.Employee;
import app.turnofacil.service.ServiceOffering;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceOffering service;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AppointmentStatus status;

    @Column(length = 2000)
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Appointment() {
    }

    private Appointment(Client client, Employee employee, ServiceOffering service,
                        LocalDate appointmentDate, LocalTime startTime, String notes) {
        this.id = UUID.randomUUID();
        reschedule(client, employee, service, appointmentDate, startTime, notes);
        this.status = AppointmentStatus.SCHEDULED;
    }

    public static Appointment create(Client client, Employee employee, ServiceOffering service,
                                     LocalDate appointmentDate, LocalTime startTime, String notes) {
        return new Appointment(client, employee, service, appointmentDate, startTime, notes);
    }

    public void reschedule(Client client, Employee employee, ServiceOffering service,
                           LocalDate appointmentDate, LocalTime startTime, String notes) {
        int duration = service.getDurationMinutes();
        long endSecond = startTime.toSecondOfDay() + duration * 60L;
        if (endSecond >= 24L * 60 * 60) {
            throw new IllegalArgumentException("La cita no puede terminar al día siguiente");
        }
        this.client = client;
        this.employee = employee;
        this.service = service;
        this.appointmentDate = appointmentDate;
        this.startTime = startTime;
        this.durationMinutes = duration;
        this.endTime = startTime.plusMinutes(duration);
        this.notes = normalizeNotes(notes);
    }

    public void changeStatus(AppointmentStatus status) {
        this.status = status;
    }

    private static String normalizeNotes(String notes) {
        if (notes == null) return null;
        String normalized = notes.trim();
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
    public Client getClient() { return client; }
    public Employee getEmployee() { return employee; }
    public ServiceOffering getService() { return service; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public int getDurationMinutes() { return durationMinutes; }
    public AppointmentStatus getStatus() { return status; }
    public String getNotes() { return notes; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
