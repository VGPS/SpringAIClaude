package com.wgblackmon.docprocessor.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * SecurityConfig
 * 
 * Security configuration class that defines authentication and authorization
 * rules for the application. Configures Spring Security features including
 * password encoding, session management, and access control.
 *
 * Key responsibilities:
 * - Configure security filter chain
 * - Set up authentication mechanisms
 * - Define authorization rules
 * - Configure password encoding
 * - Manage session policies
 *
 * @author William Blackmon
 * @version 1.0
 * @since 2024-03-01
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final Logger logger = LogManager.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring security filter chain");
        http
            .authorizeHttpRequests(auth -> {
                logger.debug("Configuring HTTP request authorization rules");
                auth
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/api/public/**").permitAll()
                    .anyRequest().authenticated();
            })
            .formLogin(form -> {
                logger.debug("Configuring form login");
                form
                    .loginPage("/login")
                    .permitAll();
            })
            .sessionManagement(session -> {
                logger.debug("Configuring session management");
                session
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .maximumSessions(1)
                    .maxSessionsPreventsLogin(true);
            });

        logger.info("Security filter chain configuration completed");
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.debug("Creating password encoder bean");
        return new BCryptPasswordEncoder();
    }
}