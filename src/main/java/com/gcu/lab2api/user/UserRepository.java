package com.gcu.lab2api.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Name: Jose Beltran Veles
 * Course: CST-339
 * Assignment: Lab 4
 * Date: October 20, 2026
 *
 * Provides database operations for application users.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByRole(Role role);

    long countByRoleAndEnabledTrue(Role role);
}
