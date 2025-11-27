package com.skilloVilla.Controller;

import com.skilloVilla.Dto.UserCreateDto;
import com.skilloVilla.Entity.AppUser;
import com.skilloVilla.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // Створення бібліотекаря (доступно тільки ADMIN)
    // POST /api/users/librarians
    @PostMapping("/librarians")
    public ResponseEntity<AppUser> createLibrarian(@Valid @RequestBody UserCreateDto dto) {
        AppUser created = userService.createLibrarian(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
