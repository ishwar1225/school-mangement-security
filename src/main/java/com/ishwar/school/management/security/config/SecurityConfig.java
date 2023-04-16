package com.ishwar.school.management.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JWTAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf().disable().
                authorizeHttpRequests().
                requestMatchers("/user-auth/**").
                permitAll().
                anyRequest().
                authenticated().
                and().
                sessionManagement().sessionCreationPolicy(STATELESS).
                and().
                authenticationProvider(authenticationProvider).
                addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).build();

    }

}
