package com.gcu.lab2api.user;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gcu.lab2api.user.viewmodel.LoginViewModel;
import com.gcu.lab2api.user.viewmodel.PasswordChangeViewModel;
import com.gcu.lab2api.user.viewmodel.RegistrationViewModel;
import com.gcu.lab2api.user.viewmodel.UserUpdateViewModel;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Name: Jose Beltran Veles
 * Course: CST-339
 * Assignment: Lab 4
 * Date: October 20, 2026
 *
 * Tests user-management rules without changing the database.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    private ValidatorFactory validatorFactory;
    private UserService service;

    @BeforeEach
    void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        service = new UserService(
                repository, validatorFactory.getValidator());
    }

    @AfterEach
    void tearDown() {
        validatorFactory.close();
    }

    @Test
    void registrationAssignsUserRoleAndTrimsUsername() {
        when(repository.saveAndFlush(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.register(registration("  jose  "));

        assertEquals("jose", result.getUsername());
        assertEquals(Role.USER, result.getRole());
        assertTrue(result.isEnabled());
        assertFalse(result.isPasswordChangeRequired());
        verify(repository).existsByUsername("jose");
    }

    @Test
    void registrationRejectsBlankUsername() {
        assertThrows(IllegalArgumentException.class,
                () -> service.register(registration("   ")));

        verifyNoInteractions(repository);
    }

    @Test
    void registrationRejectsMismatchedPasswords() {
        var form = registration("jose");
        form.setConfirmPassword("different");

        assertThrows(IllegalArgumentException.class,
                () -> service.register(form));

        verifyNoInteractions(repository);
    }

    @Test
    void registrationRejectsDuplicateUsername() {
        when(repository.existsByUsername("jose")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.register(registration("jose")));

        verify(repository, never()).saveAndFlush(any(User.class));
    }

    @Test
    void loginAcceptsCorrectPasswordAndRejectsWrongPassword() {
        when(repository.findByUsername("jose"))
                .thenReturn(Optional.of(user(Role.USER)));

        assertTrue(service.validateLogin(login("test-password")).isPresent());
        assertTrue(service.validateLogin(login("wrong-password")).isEmpty());
    }

    @Test
    void loginRejectsMissingAndDisabledAccounts() {
        when(repository.findByUsername("jose"))
                .thenReturn(Optional.empty());

        assertTrue(service.validateLogin(login("test-password")).isEmpty());

        User disabled = user(Role.USER);
        disabled.setEnabled(false);
        when(repository.findByUsername("jose"))
                .thenReturn(Optional.of(disabled));

        assertTrue(service.validateLogin(login("test-password")).isEmpty());
    }

    @Test
    void administrationRejectsInvalidRole() {
        var form = new UserUpdateViewModel();
        form.setRole("OWNER");

        assertThrows(IllegalArgumentException.class,
                () -> service.updateUser(1, form));

        verifyNoInteractions(repository);
    }

    @Test
    void administrationProtectsLastEnabledAdministrator() {
        when(repository.findById(1))
                .thenReturn(Optional.of(user(Role.ADMIN)));
        when(repository.countByRoleAndEnabledTrue(Role.ADMIN))
                .thenReturn(1L);

        var form = new UserUpdateViewModel();
        form.setRole("ADMIN");
        form.setEnabled(false);

        assertThrows(IllegalArgumentException.class,
                () -> service.updateUser(1, form));

        form.setRole("USER");
        form.setEnabled(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.updateUser(1, form));

        verify(repository, never()).saveAndFlush(any(User.class));
    }

    @Test
    void passwordChangeReplacesPasswordAndClearsRequiredFlag() {
        User user = user(Role.USER);
        when(repository.findById(1)).thenReturn(Optional.of(user));

        service.changePassword(1, passwordChange());

        assertEquals("replacement-password", user.getPassword());
        assertFalse(user.isPasswordChangeRequired());
        verify(repository).saveAndFlush(user);
    }

    @Test
    void passwordChangeRejectsWrongCurrentPasswordAndMismatch() {
        User user = user(Role.USER);
        when(repository.findById(1)).thenReturn(Optional.of(user));

        var form = passwordChange();
        form.setCurrentPassword("wrong-password");

        assertThrows(IllegalArgumentException.class,
                () -> service.changePassword(1, form));

        form.setCurrentPassword("test-password");
        form.setConfirmPassword("different");

        assertThrows(IllegalArgumentException.class,
                () -> service.changePassword(1, form));

        assertEquals("test-password", user.getPassword());
        verify(repository, never()).saveAndFlush(any(User.class));
    }

    private RegistrationViewModel registration(String username) {
        var form = new RegistrationViewModel();
        form.setUsername(username);
        form.setPassword("test-password");
        form.setConfirmPassword("test-password");
        return form;
    }

    private LoginViewModel login(String password) {
        var form = new LoginViewModel();
        form.setUsername("jose");
        form.setPassword(password);
        return form;
    }

    private PasswordChangeViewModel passwordChange() {
        var form = new PasswordChangeViewModel();
        form.setCurrentPassword("test-password");
        form.setNewPassword("replacement-password");
        form.setConfirmPassword("replacement-password");
        return form;
    }

    private User user(Role role) {
        User user = new User();
        user.setUsername("jose");
        user.setPassword("test-password");
        user.setRole(role);
        return user;
    }
}
