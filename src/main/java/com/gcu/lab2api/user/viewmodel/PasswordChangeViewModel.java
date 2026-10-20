package com.gcu.lab2api.user.viewmodel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Name: Jose Beltran Veles
 * Course: CST-339
 * Assignment: Lab 4
 * Date: October 20, 2026
 *
 * Holds password-change form input.
 */
public class PasswordChangeViewModel {

    @NotBlank(message = "Current password is required.")
    @Size(max = 255, message = "Password must be 255 characters or fewer.")
    private String currentPassword;

    @NotBlank(message = "New password is required.")
    @Size(max = 255, message = "Password must be 255 characters or fewer.")
    private String newPassword;

    @NotBlank(message = "Please confirm your new password.")
    @Size(max = 255, message = "Confirmation must be 255 characters or fewer.")
    private String confirmPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
