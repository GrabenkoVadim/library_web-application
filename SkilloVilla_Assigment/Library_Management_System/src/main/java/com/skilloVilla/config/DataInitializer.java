package com.skilloVilla.config;

import com.skilloVilla.Entity.AppUser;
import com.skilloVilla.Entity.Role;
import com.skilloVilla.Repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final AppUserRepository repo;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        if (repo.findByUsername("admin").isEmpty()) {
            AppUser user = new AppUser();
            user.setUsername("admin");
            user.setPassword(encoder.encode("admin"));
            user.setRole(Role.ADMIN);
            repo.save(user);
            System.out.println("Admin created: admin/admin");
        }
    }
}
