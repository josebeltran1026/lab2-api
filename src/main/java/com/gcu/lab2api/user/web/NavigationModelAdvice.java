package com.gcu.lab2api.user.web;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Supplies navigation visibility to the user management templates.
 */
@ControllerAdvice(basePackageClasses = LoginController.class)
public class NavigationModelAdvice {

    /**
     * Determines whether the current user should see the Administration link.
     *
     * @param request the current web request
     * @return whether the signed-in user has the ADMIN role
     */
    @ModelAttribute("showAdminLink")
    public boolean showAdminLink(HttpServletRequest request) {
        return request.isUserInRole("ADMIN");
    }
}