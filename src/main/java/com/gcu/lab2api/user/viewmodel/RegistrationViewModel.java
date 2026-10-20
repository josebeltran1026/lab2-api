package com.gcu.lab2api.user.viewmodel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Name: Jose Beltran Veles
 * Course: CST-339
 * Assignment: Lab 4
 * Date: October 20, 2026
 *
 * Holds registration form input separately from the User entity.
 */
public class RegistrationViewModel {

    @NotBlank(message = "Username is required.")
    @Size(max = 255, message = "Username must be 255 characters or fewer.")
    private String username;

    @NotBlank(message = "Password is required.")
    @Size(max = 255, message = "Password must be 255 characters or fewer.")
    private String password;

    @NotBlank(message = "Please confirm your password.")
    @Size(max = 255, message = "Confirmation must be 255 characters or fewer.")
    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
