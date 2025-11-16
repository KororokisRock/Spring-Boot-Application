package com.example.bankcards.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.bankcards.exception.ExceptionHandlingFilter;
import com.example.bankcards.security.JwtFilter;

import io.jsonwebtoken.lang.Arrays;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    
    private final JwtFilter jwtFilter;

    private final ExceptionHandlingFilter exceptionFilter;

    public SecurityConfig(JwtFilter jwtFilter, ExceptionHandlingFilter exceptionFilter) {
        this.jwtFilter = jwtFilter;
        this.exceptionFilter = exceptionFilter;
    }

    private static final String[] WHITELIST = {
        "/swagger-ui.html",
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/v3/api-docs.yaml",
        "/swagger-resources/**",
        "/webjars/**",
        "/configuration/ui",
        "/configuration/security",
        "/register", "/auth/**", "/"
    };

    private static final String[] ADMINLIST = {
        "/card/add",
        "/user/delete",
        "/card/all",
        "/card/block",
        "/card/activate",
        "/card/delete"
    };

    private static final String[] ALLOWED_ORIGINS = {
    };

    private static final String[] ALLOWED_METHODS = {
    };

    private static final String[] ALLOWED_HEADERS = {
    };

    private static final String[] EXPOSED_HEADERS = {
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .httpBasic(httpBasic -> httpBasic.disable())
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(WHITELIST).permitAll()
                .requestMatchers(ADMINLIST).hasRole("ADMIN")
                .anyRequest().authenticated())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(exceptionFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(ALLOWED_ORIGINS));
        configuration.setAllowedMethods(Arrays.asList(ALLOWED_METHODS));
        configuration.setAllowedHeaders(Arrays.asList(ALLOWED_HEADERS));
        configuration.setExposedHeaders(Arrays.asList(EXPOSED_HEADERS));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }    
}


