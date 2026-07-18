package app.turnofacil.client;

import app.turnofacil.shared.error.DuplicateResourceException;
import app.turnofacil.shared.error.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ClientService {
    private final ClientRepository clients;

    public ClientService(ClientRepository clients) {
        this.clients = clients;
    }

    @Transactional
    public ClientResponse create(CreateClientRequest request) {
        String email = Client.normalizeEmail(request.email());
        ensureEmailIsAvailable(email);
        Client client = Client.create(request.firstName(), request.lastName(), email,
                request.phone(), request.notes());
        return ClientResponse.from(clients.saveAndFlush(client));
    }

    @Transactional(readOnly = true)
    public List<ClientResponse> findAll() {
        return clients.findAllByOrderByLastNameAscFirstNameAsc().stream()
                .map(ClientResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClientResponse findById(UUID id) {
        return ClientResponse.from(getClient(id));
    }

    @Transactional
    public ClientResponse update(UUID id, UpdateClientRequest request) {
        Client client = getClient(id);
        String email = Client.normalizeEmail(request.email());
        if (clients.existsByEmailAndIdNot(email, id)) {
            throw new DuplicateResourceException("Ya existe un cliente con ese correo");
        }
        client.updateContactInformation(request.firstName(), request.lastName(), email,
                request.phone(), request.notes());
        return ClientResponse.from(clients.saveAndFlush(client));
    }

    @Transactional
    public ClientResponse changeStatus(UUID id, ChangeClientStatusRequest request) {
        Client client = getClient(id);
        client.changeActiveStatus(request.active());
        return ClientResponse.from(clients.saveAndFlush(client));
    }

    private Client getClient(UUID id) {
        return clients.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
    }

    private void ensureEmailIsAvailable(String email) {
        if (clients.existsByEmail(email)) {
            throw new DuplicateResourceException("Ya existe un cliente con ese correo");
        }
    }
}
