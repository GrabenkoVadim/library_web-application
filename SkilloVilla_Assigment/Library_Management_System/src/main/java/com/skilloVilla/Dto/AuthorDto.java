package com.skilloVilla.Dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class AuthorDto {
    private Integer id;

    @NotBlank(message = "Author full name is required")
    @Size(max = 150, message = "Full name must be at most 150 characters")
    private String fullName;

    @Size(max = 1000, message = "Biography must be at most 1000 characters")
    private String biography;
}
