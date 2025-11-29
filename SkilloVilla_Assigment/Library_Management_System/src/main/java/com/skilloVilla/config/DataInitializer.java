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
        // ADMIN
        if (repo.findByUsername("admin").isEmpty()) {
            AppUser admin = new AppUser();
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("admin"));
            admin.setRole(Role.ADMIN);
            repo.save(admin);
            System.out.println("Admin created: admin/admin");
        }

        // ✅ LIBRARIAN
        if (repo.findByUsername("librarian").isEmpty()) {
            AppUser librarian = new AppUser();
            librarian.setUsername("librarian");
            librarian.setPassword(encoder.encode("librarian"));
            librarian.setRole(Role.LIBRARIAN);
            repo.save(librarian);
            System.out.println("Librarian created: librarian/librarian");
        }
    }
}
