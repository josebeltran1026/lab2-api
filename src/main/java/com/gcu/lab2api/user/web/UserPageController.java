package com.gcu.lab2api.user.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Name: Jose Beltran Veles
 * Course: CST-339
 * Assignment: Lab 4
 * Date: October 20, 2026
 *
 * Displays the Thymeleaf user-management landing page.
 */
@Controller
public class UserPageController {

    @GetMapping("/users")
    public String home(Model model) {
        model.addAttribute("pageTitle", "User Management");
        return "users-home";
    }
}
