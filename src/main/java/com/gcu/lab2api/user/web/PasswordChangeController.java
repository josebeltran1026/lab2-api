package com.gcu.lab2api.user.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gcu.lab2api.user.UserService;
import com.gcu.lab2api.user.viewmodel.PasswordChangeViewModel;
import com.gcu.lab2api.user.viewmodel.UserSummaryViewModel;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

/**
 * Jose Beltran Veles
 * CST-339 — Lab 4
 * Handles password changes using the demonstration login state.
 * This is not a replacement for Spring Security authentication.
 */
@Controller
public class PasswordChangeController {

    private final UserService userService;

    public PasswordChangeController(UserService userService) {
        this.userService = userService;
    }

    @InitBinder("passwordChange")
    public void configureBinding(WebDataBinder binder) {
        binder.setAllowedFields(
            "currentPassword", "newPassword", "confirmPassword"
        );
    }

    @GetMapping("/change-password")
    public String showForm(
            @ModelAttribute("passwordChange") PasswordChangeViewModel form,
            java.security.Principal principal,
            Model model) {

        UserSummaryViewModel user = currentUser(principal);
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("username", user.getUsername());
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @Valid @ModelAttribute("passwordChange")
                PasswordChangeViewModel form,
            BindingResult bindingResult,
            java.security.Principal principal,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        UserSummaryViewModel user = currentUser(principal);
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("username", user.getUsername());

        if (bindingResult.hasErrors()) {
            return "change-password";
        }

        try {
            userService.changePassword(user.getUserId(), form);
        } catch (IllegalArgumentException ex) {
            bindingResult.reject("password.change.failed", ex.getMessage());
            return "change-password";
        }

        session.invalidate();
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "Password changed successfully. Please log in again."
        );

        return "redirect:/login";
    }

    private UserSummaryViewModel currentUser(java.security.Principal principal) {
    if (principal == null) {
        return null;
    }

    return userService.findByUsername(principal.getName())
            .filter(UserSummaryViewModel::isEnabled)
            .orElse(null);
    }
}