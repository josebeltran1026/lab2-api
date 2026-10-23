package com.gcu.lab2api.user.web;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gcu.lab2api.user.Role;
import com.gcu.lab2api.user.UserService;
import com.gcu.lab2api.user.viewmodel.UserSummaryViewModel;
import com.gcu.lab2api.user.viewmodel.UserUpdateViewModel;

import jakarta.validation.Valid;

/**
 * Name: Jose Beltran Veles
 * Course: CST-339
 * Assignment: Lab 4
 * Date: October 22, 2026
 *
 * Displays user summaries and processes account updates.
 * Administration routes are intentionally unprotected in Lab 4.
 */
@Controller
@RequestMapping("/admin/users")
public class UserAdminController {

    private final UserService userService;

    public UserAdminController(UserService userService) {
        this.userService = userService;
    }

    @InitBinder("userUpdate")
    public void configureUpdateBinding(WebDataBinder binder) {
        binder.setAllowedFields(
                "role", "enabled", "passwordChangeRequired");
    }

    @ModelAttribute("roles")
    public Role[] roles() {
        return Role.values();
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.listUsers());
        return "admin-users";
    }

    @GetMapping("/{userId}/edit")
    public String showEdit(
            @PathVariable Integer userId,
            Model model) {

        UserSummaryViewModel user = loadUser(userId);

        UserUpdateViewModel form = new UserUpdateViewModel();
        form.setRole(user.getRole().name());
        form.setEnabled(user.isEnabled());
        form.setPasswordChangeRequired(
                user.isPasswordChangeRequired());

        model.addAttribute("user", user);
        model.addAttribute("userUpdate", form);
        return "admin-user-edit";
    }

    @PostMapping("/{userId}/edit")
    public String updateUser(
            @PathVariable Integer userId,
            @Valid @ModelAttribute("userUpdate") UserUpdateViewModel form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        model.addAttribute("user", loadUser(userId));

        if (bindingResult.hasErrors()) {
            return "admin-user-edit";
        }

        try {
            userService.updateUser(userId, form);
        } catch (IllegalArgumentException exception) {
            bindingResult.reject(
                    "user.update.failed", exception.getMessage());
            return "admin-user-edit";
        }

        redirectAttributes.addFlashAttribute(
                "successMessage", "User updated successfully.");
        return "redirect:/admin/users";
    }

    private UserSummaryViewModel loadUser(Integer userId) {
        try {
            return userService.findById(userId);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User was not found.");
        }
    }
}