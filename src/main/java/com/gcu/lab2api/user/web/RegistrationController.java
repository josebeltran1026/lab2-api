package com.gcu.lab2api.user.web;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gcu.lab2api.user.UserService;
import com.gcu.lab2api.user.viewmodel.RegistrationViewModel;

import jakarta.validation.Valid;

/**
 * Name: Jose Beltran Veles
 * Course: CST-339
 * Assignment: Lab 4
 * Date: October 20, 2026
 *
 * Handles registration page requests through UserService.
 */
@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @InitBinder("registration")
    public void configureRegistrationBinding(WebDataBinder binder) {
        binder.setAllowedFields("username", "password", "confirmPassword");
    }

    @GetMapping("/register")
    public String showRegistration(
            @ModelAttribute("registration") RegistrationViewModel form) {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("registration") RegistrationViewModel form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            userService.register(form);
        } catch (IllegalArgumentException exception) {
            bindingResult.reject("registration.failed", exception.getMessage());
            return "register";
        }

        redirectAttributes.addFlashAttribute(
                "successMessage", "Registration successful. Please log in.");

        return "redirect:/login";
    }
}
