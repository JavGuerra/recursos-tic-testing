package com.grupo1.recursos_tic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import java.util.Collections;
import java.util.List;

/**
 * Configuración de permisos de rutas.
 */
@Configuration
@EnableWebSecurity
public class  SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AnonymousAuthenticationToken anonymousAuthenticationToken() {
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
        return new AnonymousAuthenticationToken("ANONYMOUS_USER", "ANONYMOUS_USER", authorities);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)  throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(authRequest -> authRequest
                    // Swagger
                    .requestMatchers("/api/**").permitAll()
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                    .requestMatchers(HttpMethod.GET,"/", "/error", "/auth/**",
                            "/login", "/css/**", "/js/**", "/img/**", "/webjars/**",
                            "/doc/**", "/info/**", "/favicon.ico").permitAll()

                    .requestMatchers(HttpMethod.GET,"/resources/create").authenticated()
                    .requestMatchers(HttpMethod.POST,"/resources/update/{id}").authenticated()
                    .requestMatchers(HttpMethod.DELETE,"/resources/delete/{id}").authenticated()
                    .requestMatchers(HttpMethod.DELETE,"/resources/delete").authenticated()
                    .requestMatchers("/resources/**").permitAll()

                    .requestMatchers(HttpMethod.GET,"/users/create").permitAll()
                    .requestMatchers(HttpMethod.POST,"/users").permitAll()

                    .anyRequest().authenticated()
            )

            .formLogin(formLogin -> formLogin
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .failureUrl("/login?error=true")
                    .permitAll()
            )

            .logout(logout -> logout
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
            )

            .exceptionHandling(exceptionHandling -> exceptionHandling
                    .accessDeniedPage("/error"));

        return http.build();
    }

}