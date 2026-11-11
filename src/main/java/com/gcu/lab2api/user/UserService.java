package com.gcu.lab2api.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gcu.lab2api.user.viewmodel.PasswordChangeViewModel;
import com.gcu.lab2api.user.viewmodel.RegistrationViewModel;
import com.gcu.lab2api.user.viewmodel.UserSummaryViewModel;
import com.gcu.lab2api.user.viewmodel.UserUpdateViewModel;

import jakarta.validation.Validator;

/**
 * Name: Jose Beltran Veles
 * Course: CST-339
 * Assignment: Lab 4
 * Date: October 20, 2026
 *
 * Applies user-management business rules.
 * Plain-text credential comparison is temporary for Lab 4.
 * This service does not provide secure authentication or authorization.
 */
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository repository;
    private final Validator validator;
    private final PasswordEncoder passwordEncoder;

            public UserService(
            UserRepository repository,
            Validator validator,
            PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.validator = validator;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public UserSummaryViewModel register(RegistrationViewModel form) {
        validate(form);

        if (!form.getPassword().equals(form.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords must match.");
        }

        String username = form.getUsername().strip();

        if (repository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username is already taken.");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(form.getPassword());
        user.setRole(Role.USER);
        user.setEnabled(true);
        user.setPasswordChangeRequired(false);

        return toSummary(repository.saveAndFlush(user));
    }

    public Optional<UserSummaryViewModel> findByUsername(String username) {
        if (username == null || username.isBlank()) {
            return Optional.empty();
        }

        return repository.findByUsername(username.strip())
                .map(this::toSummary);
    }

    public List<UserSummaryViewModel> listUsers() {
        return repository.findAll(Sort.by("username")).stream()
                .map(this::toSummary)
                .toList();
    }

    public UserSummaryViewModel findById(Integer userId) {
        return toSummary(requireUser(userId));
    }

    @Transactional
    public UserSummaryViewModel updateUser(
            Integer userId, UserUpdateViewModel form) {
        validate(form);

        Role requestedRole = Role.valueOf(form.getRole());
        User user = requireUser(userId);

        boolean removesEnabledAdmin = user.getRole() == Role.ADMIN
                && user.isEnabled()
                && (requestedRole != Role.ADMIN || !form.isEnabled());

        if (removesEnabledAdmin
                && repository.countByRoleAndEnabledTrue(Role.ADMIN) <= 1) {
            throw new IllegalArgumentException(
                    "The last enabled administrator cannot be demoted or disabled.");
        }

        user.setRole(requestedRole);
        user.setEnabled(form.isEnabled());
        user.setPasswordChangeRequired(form.isPasswordChangeRequired());

        return toSummary(repository.saveAndFlush(user));
    }

    @Transactional
    public void changePassword(
            Integer userId, PasswordChangeViewModel form) {
        validate(form);

        User user = requireUser(userId);

        if (!user.isEnabled()
        || !passwordEncoder.matches(form.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException(
                    "Unable to change password. Check your current credentials.");
        }

        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            throw new IllegalArgumentException("New passwords must match.");
        }

        if (form.getNewPassword().equals(form.getCurrentPassword())) {
            throw new IllegalArgumentException(
                    "New password must differ from the current password.");
        }

        user.setPassword(passwordEncoder.encode(form.getNewPassword()));
        user.setPasswordChangeRequired(false);
        repository.saveAndFlush(user);
    }

    private User requireUser(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required.");
        }

        return repository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User was not found."));
    }

    private <T> void validate(T form) {
        if (form == null) {
            throw new IllegalArgumentException("Form input is required.");
        }

        validator.validate(form).stream()
                .map(violation -> violation.getMessage())
                .sorted()
                .findFirst()
                .ifPresent(message -> {
                    throw new IllegalArgumentException(message);
                });
    }

    private UserSummaryViewModel toSummary(User user) {
        return new UserSummaryViewModel(
                user.getUserId(),
                user.getUsername(),
                user.getRole(),
                user.isEnabled(),
                user.isPasswordChangeRequired());
    }
}
