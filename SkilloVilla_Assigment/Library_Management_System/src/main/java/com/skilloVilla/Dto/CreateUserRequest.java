package com.skilloVilla.Dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CreateUserRequest {

    @NotBlank
    @Size(max = 50)
    private String username;

    @NotBlank
    @Size(min = 4, max = 100)
    private String password;

    // опціонально – дозволимо задати роль, але на фронті можна завжди ставити LIBRARIAN
    @NotBlank
    private String role;
}
