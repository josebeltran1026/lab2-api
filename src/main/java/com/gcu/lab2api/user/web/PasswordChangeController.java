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
            HttpSession session,
            Model model) {

        UserSummaryViewModel user = currentUser(session);
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
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        UserSummaryViewModel user = currentUser(session);
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

        session.removeAttribute("demoUserId");
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "Password changed successfully. Please log in again."
        );

        return "redirect:/login";
    }

    private UserSummaryViewModel currentUser(HttpSession session) {
        Object value = session.getAttribute("demoUserId");

        if (!(value instanceof Integer userId)) {
            return null;
        }

        try {
            UserSummaryViewModel user = userService.findById(userId);
            if (user.isEnabled()) {
                return user;
            }
        } catch (IllegalArgumentException ex) {
            // The demonstration account no longer exists.
        }

        session.removeAttribute("demoUserId");
        return null;
    }
}