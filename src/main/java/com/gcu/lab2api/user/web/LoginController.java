package com.gcu.lab2api.user.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.gcu.lab2api.user.viewmodel.LoginViewModel;

/**
 * Displays the login form for CST-339 Lab 5.
 * Spring Security handles form submissions.
 */
@Controller
public class LoginController {

    /**
     * Displays the sign-in page with its form model.
     *
     * @param form the model used by the login template
     * @return the login template name
     */
    @GetMapping("/login")
    public String showLogin(
            @ModelAttribute(value = "login", binding = false) LoginViewModel form) {
        return "login";
    }
}