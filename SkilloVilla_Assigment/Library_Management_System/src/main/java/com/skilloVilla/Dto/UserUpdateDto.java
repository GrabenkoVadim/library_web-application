package com.skilloVilla.Dto;

import com.skilloVilla.Entity.Role;
import lombok.Data;

@Data
public class UserUpdateDto {
    private String username;
    private String password;
    private Role role;
}
