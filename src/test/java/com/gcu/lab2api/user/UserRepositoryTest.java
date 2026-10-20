package com.gcu.lab2api.user;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Name: Jose Beltran Veles
 * Course: CST-339
 * Assignment: Lab 4
 * Date: October 20, 2026
 *
 * Verifies user persistence and repository queries.
 * Test changes are rolled back.
 */
@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void savesAndRetrievesUserByUsername() {
        User user = createUser(Role.USER);
        repository.saveAndFlush(user);
        entityManager.clear();

        User saved = repository.findByUsername(user.getUsername())
                .orElseThrow();

        assertNotNull(saved.getUserId());
        assertEquals(Role.USER, saved.getRole());
        assertTrue(saved.isEnabled());
        assertTrue(saved.isPasswordChangeRequired());
        assertTrue(repository.existsByUsername(user.getUsername()));
    }

    @Test
    void returnsEmptyForUnknownUsername() {
        String username = "missing-" + UUID.randomUUID();

        assertTrue(repository.findByUsername(username).isEmpty());
        assertFalse(repository.existsByUsername(username));
    }

    @Test
    void detectsAdminAndCountsOnlyEnabledAdmins() {
        long initialCount =
                repository.countByRoleAndEnabledTrue(Role.ADMIN);

        User admin = repository.saveAndFlush(createUser(Role.ADMIN));

        assertTrue(repository.existsByRole(Role.ADMIN));
        assertEquals(initialCount + 1,
                repository.countByRoleAndEnabledTrue(Role.ADMIN));

        admin.setEnabled(false);
        repository.saveAndFlush(admin);

        assertEquals(initialCount,
                repository.countByRoleAndEnabledTrue(Role.ADMIN));
    }

    private User createUser(Role role) {
        User user = new User();
        user.setUsername("test-" + UUID.randomUUID());
        user.setPassword("lab4-test-only");
        user.setRole(role);
        return user;
    }
}
