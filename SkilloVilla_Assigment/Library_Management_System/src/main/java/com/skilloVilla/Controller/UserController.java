package com.skilloVilla.Controller;

import com.skilloVilla.Dto.AppUserDto;
import com.skilloVilla.Dto.UserCreateDto;
import com.skilloVilla.Dto.UserUpdateDto;
import com.skilloVilla.Service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final AppUserService userService;

    // 👉 отримати всіх користувачів (ADMIN)
    @GetMapping
    public List<AppUserDto> getUsers() {
        return userService.findAll();
    }

    // 👉 отримати конкретного користувача по ID
    @GetMapping("/{id}")
    public AppUserDto getUser(@PathVariable Long id) {
        return userService.findById(id);
    }

    // 👉 створити бібліотекаря (ADMIN)
    // POST /api/users
    @PostMapping
    public ResponseEntity<AppUserDto> createLibrarian(@Valid @RequestBody UserCreateDto dto) {
        AppUserDto created = userService.createLibrarian(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public AppUserDto updateUser(@PathVariable Long id,
                                 @RequestBody UserUpdateDto dto) {

        return userService.updateUser(
                id,
                dto.getUsername(),
                dto.getPassword(),
                dto.getRole()
        );
    }

    // 👉 видалення користувача (ADMIN)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
    }
}
