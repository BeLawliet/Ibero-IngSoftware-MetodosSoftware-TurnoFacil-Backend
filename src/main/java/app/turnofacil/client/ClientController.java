package app.turnofacil.client;

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
@RequestMapping("/api/v1/clients")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientResponse> create(@Valid @RequestBody CreateClientRequest request) {
        ClientResponse response = clientService.create(request);
        return ResponseEntity.created(URI.create("/api/v1/clients/" + response.id())).body(response);
    }

    @GetMapping
    public List<ClientResponse> findAll() {
        return clientService.findAll();
    }

    @GetMapping("/{id}")
    public ClientResponse findById(@PathVariable UUID id) {
        return clientService.findById(id);
    }

    @PutMapping("/{id}")
    public ClientResponse update(@PathVariable UUID id,
                                 @Valid @RequestBody UpdateClientRequest request) {
        return clientService.update(id, request);
    }

    @PatchMapping("/{id}/status")
    public ClientResponse changeStatus(@PathVariable UUID id,
                                       @Valid @RequestBody ChangeClientStatusRequest request) {
        return clientService.changeStatus(id, request);
    }
}
