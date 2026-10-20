package com.gcu.lab2api.user.viewmodel;

import com.gcu.lab2api.user.Role;

/**
 * Name: Jose Beltran Veles
 * Course: CST-339
 * Assignment: Lab 4
 * Date: October 20, 2026
 *
 * Provides user information for display without exposing passwords.
 */
public class UserSummaryViewModel {

    private final Integer userId;
    private final String username;
    private final Role role;
    private final boolean enabled;
    private final boolean passwordChangeRequired;

    public UserSummaryViewModel(Integer userId, String username,
            Role role, boolean enabled, boolean passwordChangeRequired) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.enabled = enabled;
        this.passwordChangeRequired = passwordChangeRequired;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isPasswordChangeRequired() {
        return passwordChangeRequired;
    }
}
