package com.gcu.lab2api.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.DefaultApplicationArguments;

/**
 * Jose Beltran Veles
 * CST-339 — Lab 4
 * Tests initial administrator creation and existing-account protection.
 */
class AdminBootstrapTest {

    private UserRepository repository;
    private AdminBootstrap bootstrap;

    @BeforeEach
    void setUp() {
        repository = mock(UserRepository.class);
        bootstrap = new AdminBootstrap(repository);
    }

    @Test
    void createsDefaultAdminWhenNoAdminExists() {
        when(repository.existsByRole(Role.ADMIN)).thenReturn(false);
        when(repository.existsByUsername("admin")).thenReturn(false);

        bootstrap.run(new DefaultApplicationArguments(new String[0]));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(repository).saveAndFlush(captor.capture());

        User admin = captor.getValue();
        assertAll(
            () -> assertEquals("admin", admin.getUsername()),
            () -> assertEquals("password", admin.getPassword()),
            () -> assertEquals(Role.ADMIN, admin.getRole()),
            () -> assertTrue(admin.isEnabled()),
            () -> assertTrue(admin.isPasswordChangeRequired())
        );
    }

    @Test
    void leavesExistingAdminUnchanged() {
        when(repository.existsByRole(Role.ADMIN)).thenReturn(true);

        bootstrap.run(new DefaultApplicationArguments(new String[0]));

        verify(repository).existsByRole(Role.ADMIN);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void rejectsUsernameCollisionWithoutPromotingExistingUser() {
        when(repository.existsByRole(Role.ADMIN)).thenReturn(false);
        when(repository.existsByUsername("admin")).thenReturn(true);

        assertThrows(
            IllegalStateException.class,
            () -> bootstrap.run(
                new DefaultApplicationArguments(new String[0])
            )
        );

        verify(repository, never()).saveAndFlush(any(User.class));
    }
}