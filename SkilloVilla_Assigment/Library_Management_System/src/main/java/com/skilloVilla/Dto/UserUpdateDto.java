package com.skilloVilla.Dto;

import com.skilloVilla.Entity.Role;
import lombok.Data;

@Data
public class UserUpdateDto {
    private String username;   // новий логін
    private String password;   // новий пароль (може бути null/порожній)
    private Role role;         // за бажанням: можна дозволити зміну ролі або ігнорити
}
