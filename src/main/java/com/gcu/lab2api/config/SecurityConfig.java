package com.gcu.lab2api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import jakarta.servlet.DispatcherType;

/**
 * Configures public routes and authentication for the browser and API.
 */
@Configuration
public class SecurityConfig {

    /**
     * Defines which requests are public and how protected requests authenticate.
     *
     * @param http the HTTP security configuration
     * @return the configured security filter chain
     * @throws Exception if the filter chain cannot be built
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        BasicAuthenticationEntryPoint apiEntryPoint = new BasicAuthenticationEntryPoint();
        apiEntryPoint.setRealmName("Chinook API");

        var apiRequests = PathPatternRequestMatcher.pathPattern("/api/**");
                 

        http.authorizeHttpRequests(authorize -> authorize
                    .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                    .requestMatchers(
                            "/", "/index.html", "/register",
                            "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**",
                            "/css/**", "/js/**", "/images/**", "/webjars/**",
                            "/favicon.ico")
                    .permitAll()
                    .anyRequest().authenticated())
                .formLogin(form -> form
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/users", true)
                    .permitAll())
                .httpBasic(Customizer.withDefaults())
                .requestCache(cache -> cache.disable())
                .exceptionHandling(exceptions -> exceptions
                        .defaultAuthenticationEntryPointFor(apiEntryPoint, apiRequests))
                .logout(logout -> logout
                    .logoutSuccessUrl("/login?logout")
                    .permitAll());

        return http.build();
    }
}