package com.saurabh.crm.security;

import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter filter;

    public SecurityConfig(JwtAuthenticationFilter f) {
        filter = f;
    }

    @Bean
    SecurityFilterChain chain(HttpSecurity http) throws Exception {
        return http.csrf(c -> c.disable()).headers(h -> h.frameOptions(f -> f.sameOrigin())).sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(a -> a.requestMatchers("/api/auth/login", "/api/auth/register", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/h2-console/**").permitAll().requestMatchers(HttpMethod.POST, "/api/auth/users").hasRole("ADMIN").anyRequest().authenticated()).addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class).build();
    }

    @Bean
    PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager manager(AuthenticationConfiguration c) throws Exception {
        return c.getAuthenticationManager();
    }
}
