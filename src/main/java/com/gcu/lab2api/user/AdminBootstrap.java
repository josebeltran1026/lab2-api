package com.gcu.lab2api.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Creates the initial administrator when a BCrypt hash is configured.
 */
@Component
public class AdminBootstrap implements ApplicationRunner {

    private final UserRepository userRepository;
    private final String bootstrapAdminHash;

    /**
     * Configures the initial administrator setup.
     *
     * @param userRepository the repository used to check and create accounts
     * @param bootstrapAdminHash the configured BCrypt hash, or an empty string
     */
    public AdminBootstrap(
            UserRepository userRepository,
            @Value("${LAB5_BOOTSTRAP_ADMIN_HASH:}") String bootstrapAdminHash) {
        this.userRepository = userRepository;
        this.bootstrapAdminHash = bootstrapAdminHash;
    }

    /**
     * Creates the initial administrator when the database has no admin.
     *
     * @param args the application startup arguments
     */
    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userRepository.existsByRole(Role.ADMIN) || bootstrapAdminHash.isBlank()) {
            return;
        }

        if (!bootstrapAdminHash.matches(
                "\\$2[aby]\\$(?:0[4-9]|[12][0-9]|3[01])\\$[./A-Za-z0-9]{53}")) {
            throw new IllegalStateException(
                    "LAB5_BOOTSTRAP_ADMIN_HASH must contain a valid BCrypt hash.");
        }

        if (userRepository.existsByUsername("admin")) {
            throw new IllegalStateException(
                    "Cannot create the administrator because username 'admin' is already in use.");
        }

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(bootstrapAdminHash);
        admin.setRole(Role.ADMIN);
        admin.setEnabled(true);
        admin.setPasswordChangeRequired(true);
        userRepository.saveAndFlush(admin);
    }
}