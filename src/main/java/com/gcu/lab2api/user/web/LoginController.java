package com.gcu.lab2api.user.web;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gcu.lab2api.user.UserService;
import com.gcu.lab2api.user.viewmodel.LoginViewModel;
import com.gcu.lab2api.user.viewmodel.UserSummaryViewModel;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

/**
 * Name: Jose Beltran Veles
 * Course: CST-339
 * Assignment: Lab 4
 * Date: October 22, 2026
 *
 * Demonstrates manual credential checking through UserService.
 * Session data supports demo navigation only.
 * Lab 4 does not enforce authentication or role authorization.
 */
@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @InitBinder("login")
    public void configureLoginBinding(WebDataBinder binder) {
        binder.setAllowedFields("username", "password");
    }

    @GetMapping("/login")
    public String showLogin(
            @ModelAttribute("login") LoginViewModel form) {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute("login") LoginViewModel form,
            BindingResult bindingResult,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        session.removeAttribute("demoUserId");

        if (bindingResult.hasErrors()) {
            return "login";
        }

        Optional<UserSummaryViewModel> result =
                userService.validateLogin(form);

        if (result.isEmpty()) {
            bindingResult.reject(
                    "login.failed",
                    "Invalid username or password, or account is disabled.");
            return "login";
        }

        UserSummaryViewModel user = result.get();

        // Used only to carry the selected account through the demo.
        session.setAttribute("demoUserId", user.getUserId());

        if (user.isPasswordChangeRequired()) {
            return "redirect:/change-password";
        }

        redirectAttributes.addFlashAttribute(
                "successMessage", "Login successful.");
        return "redirect:/users";
    }
}