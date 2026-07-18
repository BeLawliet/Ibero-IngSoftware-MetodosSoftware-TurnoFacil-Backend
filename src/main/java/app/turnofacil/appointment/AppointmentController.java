package app.turnofacil.appointment;

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
@RequestMapping("/api/v1/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> create(@Valid @RequestBody CreateAppointmentRequest request) {
        AppointmentResponse response = appointmentService.create(request);
        return ResponseEntity.created(URI.create("/api/v1/appointments/" + response.id())).body(response);
    }

    @GetMapping
    public List<AppointmentResponse> findAll() {
        return appointmentService.findAll();
    }

    @GetMapping("/{id}")
    public AppointmentResponse findById(@PathVariable UUID id) {
        return appointmentService.findById(id);
    }

    @PutMapping("/{id}")
    public AppointmentResponse update(@PathVariable UUID id,
                                      @Valid @RequestBody UpdateAppointmentRequest request) {
        return appointmentService.update(id, request);
    }

    @PatchMapping("/{id}/status")
    public AppointmentResponse changeStatus(
            @PathVariable UUID id,
            @Valid @RequestBody ChangeAppointmentStatusRequest request) {
        return appointmentService.changeStatus(id, request);
    }
}
