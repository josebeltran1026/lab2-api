package com.gcu.lab2api.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@SpringBootTest
@AutoConfigureMockMvc
class AdminAuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "USER")
    void userCannotOpenAdministration() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isForbidden());
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @WithMockUser(roles = "USER")
    void userCannotSubmitAnAdminEdit() throws Exception {
        mockMvc.perform(post("/admin/users/{userId}/edit", 1).with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userDoesNotSeeAdministrationLink() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("href=\"/admin/users\""))));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanOpenAdministration() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk());
    }

        @Test
    @Transactional
    @WithMockUser(roles = "ADMIN")
    void adminCanDisableAnAccount() throws Exception {
        User account = new User();
        account.setUsername("lab5_admin_edit_test");
        account.setPassword(passwordEncoder.encode("local-test-password"));
        account.setRole(Role.USER);
        account.setEnabled(true);
        account = userRepository.saveAndFlush(account);

        mockMvc.perform(post("/admin/users/{userId}/edit", account.getUserId())
                        .with(csrf())
                        .param("role", "USER")
                        .param("enabled", "false")
                        .param("passwordChangeRequired", "false"))
                .andExpect(redirectedUrl("/admin/users"));

        assertFalse(userRepository.findById(account.getUserId())
                .orElseThrow().isEnabled());
    }
}