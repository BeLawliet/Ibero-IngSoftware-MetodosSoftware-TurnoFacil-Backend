package app.turnofacil.appointment;

import app.turnofacil.client.Client;
import app.turnofacil.client.ClientRepository;
import app.turnofacil.employee.Employee;
import app.turnofacil.employee.EmployeeRepository;
import app.turnofacil.service.ServiceOffering;
import app.turnofacil.service.ServiceOfferingRepository;
import app.turnofacil.shared.error.BusinessRuleException;
import app.turnofacil.shared.error.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
public class AppointmentService {
    private final AppointmentRepository appointments;
    private final ClientRepository clients;
    private final EmployeeRepository employees;
    private final ServiceOfferingRepository services;
    private final ZoneId applicationZone;

    public AppointmentService(AppointmentRepository appointments, ClientRepository clients,
                              EmployeeRepository employees, ServiceOfferingRepository services,
                              @Value("${app.time-zone:America/Bogota}") String timeZone) {
        this.appointments = appointments;
        this.clients = clients;
        this.employees = employees;
        this.services = services;
        this.applicationZone = ZoneId.of(timeZone);
    }

    @Transactional
    public AppointmentResponse create(CreateAppointmentRequest request) {
        RelatedEntities related = loadActiveRelatedEntities(request.clientId(), request.employeeId(),
                request.serviceId());
        validateSchedule(request.appointmentDate(), request.startTime(), related.service(),
                related.employee().getId(), null);
        Appointment appointment = Appointment.create(related.client(), related.employee(), related.service(),
                request.appointmentDate(), request.startTime(), request.notes());
        return AppointmentResponse.from(appointments.saveAndFlush(appointment));
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> findAll() {
        return appointments.findAllByOrderByAppointmentDateAscStartTimeAsc().stream()
                .map(AppointmentResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public AppointmentResponse findById(UUID id) {
        return AppointmentResponse.from(getAppointment(id));
    }

    @Transactional
    public AppointmentResponse update(UUID id, UpdateAppointmentRequest request) {
        Appointment appointment = getAppointment(id);
        RelatedEntities related = loadActiveRelatedEntities(request.clientId(), request.employeeId(),
                request.serviceId());
        validateSchedule(request.appointmentDate(), request.startTime(), related.service(),
                related.employee().getId(), id);
        appointment.reschedule(related.client(), related.employee(), related.service(),
                request.appointmentDate(), request.startTime(), request.notes());
        return AppointmentResponse.from(appointments.saveAndFlush(appointment));
    }

    @Transactional
    public AppointmentResponse changeStatus(UUID id, ChangeAppointmentStatusRequest request) {
        Appointment appointment = getAppointment(id);
        appointment.changeStatus(request.status());
        return AppointmentResponse.from(appointments.saveAndFlush(appointment));
    }

    private void validateSchedule(LocalDate date, LocalTime startTime, ServiceOffering service,
                                  UUID employeeId, UUID excludedId) {
        if (date.isBefore(LocalDate.now(applicationZone))) {
            throw new BusinessRuleException("No se puede programar una cita en una fecha pasada");
        }
        long endSecond = startTime.toSecondOfDay() + service.getDurationMinutes() * 60L;
        if (endSecond >= 24L * 60 * 60) {
            throw new BusinessRuleException("La cita no puede terminar al día siguiente");
        }
        LocalTime endTime = startTime.plusMinutes(service.getDurationMinutes());
        if (appointments.countEmployeeOverlaps(employeeId, date, startTime, endTime, excludedId) > 0) {
            throw new BusinessRuleException("El empleado ya tiene una cita en ese horario");
        }
    }

    private RelatedEntities loadActiveRelatedEntities(UUID clientId, UUID employeeId, UUID serviceId) {
        Client client = clients.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
        Employee employee = employees.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado"));
        ServiceOffering service = services.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));
        if (!client.isActive()) throw new BusinessRuleException("El cliente está inactivo");
        if (!employee.isActive()) throw new BusinessRuleException("El empleado está inactivo");
        if (!service.isActive()) throw new BusinessRuleException("El servicio está inactivo");
        return new RelatedEntities(client, employee, service);
    }

    private Appointment getAppointment(UUID id) {
        return appointments.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));
    }

    private record RelatedEntities(Client client, Employee employee, ServiceOffering service) {}
}
