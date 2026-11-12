package com.gcu.lab2api.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.mock.web.MockHttpSession;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

    @Test
    void flaggedAccountMustChangePasswordBeforeUsingProtectedRoutes() throws Exception {
        saveAccount(true);
        User account = userRepository.findByUsername(USERNAME).orElseThrow();
        account.setPasswordChangeRequired(true);
        userRepository.saveAndFlush(account);

        var loginResult = mockMvc.perform(formLogin().user(USERNAME).password(PASSWORD))
                .andExpect(authenticated().withUsername(USERNAME))
                .andReturn();

        var session = (MockHttpSession) loginResult.getRequest().getSession(false);

        mockMvc.perform(get("/users").session(session))
                .andExpect(redirectedUrl("/change-password"));

        mockMvc.perform(get("/change-password").session(session))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/albums/1").with(httpBasic(USERNAME, PASSWORD)))
                .andExpect(status().isForbidden());
                String newPassword = "lab5-replacement-test-password";

        mockMvc.perform(post("/change-password").session(session).with(csrf())
                        .param("currentPassword", PASSWORD)
                        .param("newPassword", newPassword)
                        .param("confirmPassword", newPassword))
                .andExpect(redirectedUrl("/login"));

        var newLogin = mockMvc.perform(formLogin().user(USERNAME).password(newPassword))
                .andExpect(authenticated().withUsername(USERNAME))
                .andReturn();

        var newSession = (MockHttpSession) newLogin.getRequest().getSession(false);
        mockMvc.perform(get("/users").session(newSession))
                .andExpect(status().isOk());
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