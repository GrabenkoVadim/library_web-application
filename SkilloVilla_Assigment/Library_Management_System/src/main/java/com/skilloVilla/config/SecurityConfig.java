package com.skilloVilla.config;

import com.skilloVilla.Service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // логін + статика
                        .requestMatchers(
                                new AntPathRequestMatcher("/login.html"),
                                new AntPathRequestMatcher("/css/**"),
                                new AntPathRequestMatcher("/js/**"),
                                new AntPathRequestMatcher("/images/**")
                        ).permitAll()

                        // керування бібліотекарями (акаунти) – тільки ADMIN
                        .requestMatchers(
                                new AntPathRequestMatcher("/api/users/**")
                        ).hasRole("ADMIN")

                        // логи дій – тільки ADMIN
                        .requestMatchers(
                                new AntPathRequestMatcher("/api/logs/**")
                        ).hasRole("ADMIN")

                        // книги / читачі / видача – бібліотекарі + адміни
                        .requestMatchers(
                                new AntPathRequestMatcher("/api/books/**"),
                                new AntPathRequestMatcher("/api/readers/**"),
                                new AntPathRequestMatcher("/api/loans/**"),
                                new AntPathRequestMatcher("/api/collections/**")   // 🔹 додали
                        ).hasAnyRole("LIBRARIAN", "ADMIN")


                        // все інше – просто автентифіковані
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login.html")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/index.html", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login.html?logout")
                        .permitAll()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
