package com.skilloVilla.Controller;

import com.skilloVilla.Entity.AppUser;
import com.skilloVilla.Service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MeController {

    private final AppUserService userService;

    @GetMapping("/api/me")
    public Map<String, Object> me(Authentication authentication) {
        AppUser user = userService.findEntityByUsername(authentication.getName());

        Map<String, Object> result = new HashMap<>();
        result.put("id", user.getId());
        result.put("username", user.getUsername());
        result.put("role", user.getRole().name());
        return result;
    }

}
