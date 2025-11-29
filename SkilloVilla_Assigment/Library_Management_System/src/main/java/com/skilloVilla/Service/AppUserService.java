package com.skilloVilla.Service;

import com.skilloVilla.Dto.AppUserDto;
import com.skilloVilla.Dto.UserCreateDto;
import com.skilloVilla.Entity.AppUser;
import com.skilloVilla.Entity.Role;
import com.skilloVilla.Exception.NotFoundException;
import com.skilloVilla.Repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {

    private final AppUserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final ActionLogService actionLogService;

    // ====== для Spring Security ======
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // AppUser implements UserDetails, тож повертаємо його напряму
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username " + username
                ));
    }

    /** Якщо треба саме сутність, а не UserDetails */
    public AppUser findEntityByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    // ====== методи для адмінки (робота з юзерами) ======

    /** Повернути всіх користувачів як DTO */
    public List<AppUserDto> findAll() {
        return userRepo.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    /** Повернути одного користувача за id */
    public AppUserDto findById(Long id) {
        AppUser user = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id " + id));
        return toDto(user);
    }

    /** Створити бібліотекаря (ROLE_LIBRARIAN) */
    public AppUserDto createLibrarian(UserCreateDto dto) {
        String username = dto.getUsername();

        if (userRepo.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }

        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.LIBRARIAN);
        // enabled у нас завжди true через isEnabled() в AppUser

        AppUser saved = userRepo.save(user);
        return toDto(saved);
    }

    /** Видалити користувача */
    public void deleteById(Long id) {
        // 1. Хто зараз залогінений
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            AppUser current = userRepo.findByUsername(auth.getName()).orElse(null);

            // 2. Якщо намагаємось видалити свій власний акаунт — кидаємо помилку
            if (current != null && current.getId() != null && current.getId().equals(id)) {
                throw new IllegalStateException("You cannot delete your own account");
            }
        }

        // 3. Стандартне видалення
        if (!userRepo.existsById(id)) {
            throw new NotFoundException("User not found with id " + id);
        }

        userRepo.deleteById(id);

        // 4. Лог в журнал дій
        actionLogService.log("DELETE_USER", "AppUser", id.intValue());
    }

    public AppUserDto updateUser(Long id, String newUsername, String newRawPassword, Role newRole) {
        AppUser user = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id " + id));

        // перевірка унікальності username, якщо змінюємо
        if (newUsername != null && !newUsername.isBlank()
                && !newUsername.equals(user.getUsername())) {

            boolean exists = userRepo.findByUsername(newUsername).isPresent();
            if (exists) {
                throw new IllegalArgumentException("Username already exists: " + newUsername);
            }
            user.setUsername(newUsername);
        }

        // оновлення пароля тільки якщо щось передали
        if (newRawPassword != null && !newRawPassword.isBlank()) {
            user.setPassword(passwordEncoder.encode(newRawPassword));
        }

        // якщо хочеш дозволити зміну ролі — розкоментуй
        if (newRole != null) {
            user.setRole(newRole);
        }

        AppUser saved = userRepo.save(user);
        return toDto(saved);
    }

    // приклад toDto, якщо ще немає / інший — підлаштуй
    private AppUserDto toDto(AppUser user) {
        AppUserDto dto = new AppUserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole().name());
        return dto;
    }
}