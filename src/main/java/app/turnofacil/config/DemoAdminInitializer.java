package app.turnofacil.config;

import app.turnofacil.user.User;
import app.turnofacil.user.UserRepository;
import app.turnofacil.user.UserRole;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DemoAdminInitializer implements ApplicationRunner {
    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final TurnoFacilProperties properties;

    public DemoAdminInitializer(UserRepository users, PasswordEncoder passwordEncoder,
                                TurnoFacilProperties properties) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.properties = properties;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        var admin = properties.demoAdmin();
        String email = User.normalizeEmail(admin.email());
        if (!users.existsByEmail(email)) {
            users.save(User.create(admin.name(), email, passwordEncoder.encode(admin.password()),
                    UserRole.ADMIN, true));
        }
    }
}
