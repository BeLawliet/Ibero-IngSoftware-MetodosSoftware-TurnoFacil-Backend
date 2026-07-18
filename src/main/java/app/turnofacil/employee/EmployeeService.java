package app.turnofacil.employee;

import app.turnofacil.shared.error.DuplicateResourceException;
import app.turnofacil.shared.error.BusinessRuleException;
import app.turnofacil.shared.error.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeService {
    private final EmployeeRepository employees;

    public EmployeeService(EmployeeRepository employees) {
        this.employees = employees;
    }

    @Transactional
    public EmployeeResponse create(CreateEmployeeRequest request) {
        String email = Employee.normalizeEmail(request.email());
        ensureEmailIsAvailable(email);
        Employee employee = Employee.create(request.firstName(), request.lastName(), email,
                request.phone(), request.specialty(), request.color(), request.notes());
        return EmployeeResponse.from(employees.saveAndFlush(employee));
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponse> findAll() {
        return employees.findAllByOrderByLastNameAscFirstNameAsc().stream()
                .map(EmployeeResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public EmployeeResponse findById(UUID id) {
        return EmployeeResponse.from(getEmployee(id));
    }

    @Transactional
    public EmployeeResponse update(UUID id, UpdateEmployeeRequest request) {
        Employee employee = getEmployee(id);
        String email = Employee.normalizeEmail(request.email());
        if (employees.existsByEmailAndIdNot(email, id)) {
            throw new DuplicateResourceException("Ya existe un empleado con ese correo");
        }
        employee.updateContactInformation(request.firstName(), request.lastName(), email,
                request.phone(), request.specialty(), request.color(), request.notes());
        return EmployeeResponse.from(employees.saveAndFlush(employee));
    }

    @Transactional
    public EmployeeResponse changeStatus(UUID id, ChangeEmployeeStatusRequest request) {
        Employee employee = getEmployee(id);
        employee.changeActiveStatus(request.active());
        return EmployeeResponse.from(employees.saveAndFlush(employee));
    }

    @Transactional
    public EmployeeResponse changeAvailability(UUID id, ChangeEmployeeAvailabilityRequest request) {
        Employee employee = getEmployee(id);
        if (!employee.isActive() && request.availability() != EmployeeAvailability.OFF_SHIFT) {
            throw new BusinessRuleException("Un empleado inactivo debe permanecer fuera de turno");
        }
        employee.changeAvailability(request.availability());
        return EmployeeResponse.from(employees.saveAndFlush(employee));
    }

    private Employee getEmployee(UUID id) {
        return employees.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado"));
    }

    private void ensureEmailIsAvailable(String email) {
        if (employees.existsByEmail(email)) {
            throw new DuplicateResourceException("Ya existe un empleado con ese correo");
        }
    }
}
