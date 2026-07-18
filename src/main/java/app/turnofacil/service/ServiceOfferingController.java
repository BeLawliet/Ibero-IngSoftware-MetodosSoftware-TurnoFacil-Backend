package app.turnofacil.service;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/services")
public class ServiceOfferingController {
    private final ServiceOfferingService serviceOfferingService;

    public ServiceOfferingController(ServiceOfferingService serviceOfferingService) {
        this.serviceOfferingService = serviceOfferingService;
    }

    @PostMapping
    public ResponseEntity<ServiceOfferingResponse> create(
            @Valid @RequestBody CreateServiceOfferingRequest request) {
        ServiceOfferingResponse response = serviceOfferingService.create(request);
        return ResponseEntity.created(URI.create("/api/v1/services/" + response.id())).body(response);
    }

    @GetMapping
    public List<ServiceOfferingResponse> findAll() {
        return serviceOfferingService.findAll();
    }

    @GetMapping("/{id}")
    public ServiceOfferingResponse findById(@PathVariable UUID id) {
        return serviceOfferingService.findById(id);
    }

    @PutMapping("/{id}")
    public ServiceOfferingResponse update(@PathVariable UUID id,
                                          @Valid @RequestBody UpdateServiceOfferingRequest request) {
        return serviceOfferingService.update(id, request);
    }

    @PatchMapping("/{id}/status")
    public ServiceOfferingResponse changeStatus(
            @PathVariable UUID id,
            @Valid @RequestBody ChangeServiceOfferingStatusRequest request) {
        return serviceOfferingService.changeStatus(id, request);
    }
}
