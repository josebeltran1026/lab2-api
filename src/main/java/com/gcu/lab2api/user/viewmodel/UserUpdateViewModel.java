package com.gcu.lab2api.user.viewmodel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Name: Jose Beltran Veles
 * Course: CST-339
 * Assignment: Lab 4
 * Date: October 20, 2026
 *
 * Holds account administration updates without password values.
 */
public class UserUpdateViewModel {

    @NotBlank(message = "Role is required.")
    @Pattern(regexp = "ADMIN|USER", message = "Role must be ADMIN or USER.")
    private String role;

    private boolean enabled;

    private boolean passwordChangeRequired;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isPasswordChangeRequired() {
        return passwordChangeRequired;
    }

    public void setPasswordChangeRequired(boolean passwordChangeRequired) {
        this.passwordChangeRequired = passwordChangeRequired;
    }
}
