package com.gcu.lab2api.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class DatabaseAuthenticationTest {

    private static final String USERNAME = "lab5_bcrypt_test";
    private static final String PASSWORD = "lab5-test-password";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void acceptsCorrectPassword() throws Exception {
        saveAccount(true);

        mockMvc.perform(formLogin().user(USERNAME).password(PASSWORD))
                .andExpect(authenticated().withUsername(USERNAME))
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    void rejectsIncorrectPassword() throws Exception {
        saveAccount(true);

        mockMvc.perform(formLogin().user(USERNAME).password("wrong-password"))
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    void rejectsDisabledAccount() throws Exception {
        saveAccount(false);

        mockMvc.perform(formLogin().user(USERNAME).password(PASSWORD))
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    void rejectsUnknownUsername() throws Exception {
        mockMvc.perform(formLogin().user("lab5_missing_account").password(PASSWORD))
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/login?error"));
    }

    private void saveAccount(boolean enabled) {
        User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(passwordEncoder.encode(PASSWORD));
        user.setRole(Role.USER);
        user.setEnabled(enabled);
        user.setPasswordChangeRequired(false);
        userRepository.saveAndFlush(user);
    }
}