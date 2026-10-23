package com.gcu.lab2api.user;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Jose Beltran Veles
 * CST-339 — Lab 4
 * Creates the initial administrator when no ADMIN exists.
 * Plaintext passwords are intentional for this lab.
 */
@Component
public class AdminBootstrap implements ApplicationRunner {

    private final UserRepository userRepository;

    public AdminBootstrap(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userRepository.existsByRole(Role.ADMIN)) {
            return;
        }

        if (userRepository.existsByUsername("admin")) {
            throw new IllegalStateException(
                "Cannot create the default administrator: username 'admin' "
                + "already belongs to a non-admin account."
            );
        }

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("password");
        admin.setRole(Role.ADMIN);
        admin.setEnabled(true);
        admin.setPasswordChangeRequired(true);

        userRepository.saveAndFlush(admin);
    }
}