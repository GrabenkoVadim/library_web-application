package com.skilloVilla.config;

import com.skilloVilla.Service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final AppUserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // логін / статика — без авторизації
                        .requestMatchers(
                                new AntPathRequestMatcher("/"),
                                new AntPathRequestMatcher("/index.html"),
                                new AntPathRequestMatcher("/css/**"),
                                new AntPathRequestMatcher("/js/**"),
                                new AntPathRequestMatcher("/images/**")
                        ).permitAll()

                        // створення бібліотекарів — тільки ADMIN
                        .requestMatchers(
                                new AntPathRequestMatcher("/api/users/librarians", "POST")
                        ).hasRole("ADMIN")

                        // логи дій — тільки ADMIN
                        .requestMatchers(
                                new AntPathRequestMatcher("/api/logs/**")
                        ).hasRole("ADMIN")

                        // увесь інший бекенд API — ADMIN або LIBRARIAN
                        .requestMatchers(
                                new AntPathRequestMatcher("/api/**")
                        ).hasAnyRole("ADMIN", "LIBRARIAN")

                        // все інше — просто автентифікація
                        .anyRequest().authenticated()
                )
                .userDetailsService(userService)
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .httpBasic(Customizer.withDefaults())
                .logout(LogoutConfigurer::permitAll);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
