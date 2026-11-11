package com.gcu.lab2api.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Loads database users for Spring Security.
 */
@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Creates a user lookup service backed by the application repository.
     *
     * @param userRepository the repository used to look up accounts
     */
    public DatabaseUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads the account's stored password, role, and enabled status.
     *
     * @param username the username submitted at login
     * @return the account details used by Spring Security
     * @throws UsernameNotFoundException if the account does not exist
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        User account = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles(account.getRole().name())
                .disabled(!account.isEnabled())
                .build();
    }
}