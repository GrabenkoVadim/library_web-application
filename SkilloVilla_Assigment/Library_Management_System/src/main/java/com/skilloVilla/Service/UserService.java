package com.skilloVilla.Service;

import com.skilloVilla.Dto.UserCreateDto;
import com.skilloVilla.Entity.AppUser;
import com.skilloVilla.Entity.Role;
import com.skilloVilla.Exception.NotFoundException;
import com.skilloVilla.Repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUser createLibrarian(UserCreateDto dto) {
        AppUser user = new AppUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole() != null ? dto.getRole() : Role.LIBRARIAN);
        return appUserRepository.save(user);
    }
}
