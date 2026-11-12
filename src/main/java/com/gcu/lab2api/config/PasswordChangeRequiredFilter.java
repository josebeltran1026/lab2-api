package com.gcu.lab2api.config;

import java.io.IOException;

import com.gcu.lab2api.user.UserService;
import com.gcu.lab2api.user.viewmodel.UserSummaryViewModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Restricts protected routes until the signed-in user changes their password.
 */
final class PasswordChangeRequiredFilter extends OncePerRequestFilter {

    private static final PathPatternRequestMatcher API =
            PathPatternRequestMatcher.pathPattern("/api/**");
    private static final PathPatternRequestMatcher USER_HOME =
            PathPatternRequestMatcher.pathPattern("/users");
    private static final PathPatternRequestMatcher USER_ADMIN =
            PathPatternRequestMatcher.pathPattern("/admin/users/**");

    private final UserService userService;

    PasswordChangeRequiredFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        var principal = request.getUserPrincipal();
        boolean apiRequest = API.matches(request);
        boolean protectedPage = USER_HOME.matches(request) || USER_ADMIN.matches(request);

        if (principal != null && (apiRequest || protectedPage)
                && userService.findByUsername(principal.getName())
                        .map(UserSummaryViewModel::isPasswordChangeRequired)
                        .orElse(false)) {
            if (apiRequest) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Password change required");
            } else {
                response.sendRedirect(request.getContextPath() + "/change-password");
            }
            return;
        }

        chain.doFilter(request, response);
    }
}