package app.turnofacil.client;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
    Optional<Client> findByEmail(String normalizedEmail);
    boolean existsByEmail(String normalizedEmail);
    boolean existsByEmailAndIdNot(String normalizedEmail, UUID id);
    List<Client> findAllByOrderByLastNameAscFirstNameAsc();
}
