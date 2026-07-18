package app.turnofacil.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

@ConfigurationProperties(prefix = "turnofacil")
public record TurnoFacilProperties(Cors cors, Jwt jwt, DemoAdmin demoAdmin) {
    public record Cors(List<String> allowedOrigins) {}
    public record Jwt(String issuer, String secret, Duration expiration) {}
    public record DemoAdmin(String email, String name, String password) {}
}
