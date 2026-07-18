package app.turnofacil.service;

import app.turnofacil.shared.error.DuplicateResourceException;
import app.turnofacil.shared.error.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ServiceOfferingService {
    private final ServiceOfferingRepository services;

    public ServiceOfferingService(ServiceOfferingRepository services) {
        this.services = services;
    }

    @Transactional
    public ServiceOfferingResponse create(CreateServiceOfferingRequest request) {
        String name = ServiceOffering.normalizeName(request.name());
        ensureNameIsAvailable(name);
        ServiceOffering service = ServiceOffering.create(name, request.description(), request.category(),
                request.durationMinutes(), request.price(), request.color());
        return ServiceOfferingResponse.from(services.saveAndFlush(service));
    }

    @Transactional(readOnly = true)
    public List<ServiceOfferingResponse> findAll() {
        return services.findAllByOrderByNameAsc().stream()
                .map(ServiceOfferingResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ServiceOfferingResponse findById(UUID id) {
        return ServiceOfferingResponse.from(getService(id));
    }

    @Transactional
    public ServiceOfferingResponse update(UUID id, UpdateServiceOfferingRequest request) {
        ServiceOffering service = getService(id);
        String name = ServiceOffering.normalizeName(request.name());
        if (services.existsByNameIgnoreCaseAndIdNot(name, id)) {
            throw new DuplicateResourceException("Ya existe un servicio con ese nombre");
        }
        service.updateDetails(name, request.description(), request.category(), request.durationMinutes(),
                request.price(), request.color());
        return ServiceOfferingResponse.from(services.saveAndFlush(service));
    }

    @Transactional
    public ServiceOfferingResponse changeStatus(UUID id, ChangeServiceOfferingStatusRequest request) {
        ServiceOffering service = getService(id);
        service.changeActiveStatus(request.active());
        return ServiceOfferingResponse.from(services.saveAndFlush(service));
    }

    private ServiceOffering getService(UUID id) {
        return services.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));
    }

    private void ensureNameIsAvailable(String name) {
        if (services.existsByNameIgnoreCase(name)) {
            throw new DuplicateResourceException("Ya existe un servicio con ese nombre");
        }
    }
}
