package com.skilloVilla.Dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class BookCollectionDto {
    private Integer id;

    @NotBlank(message = "Title is required")
    @Size(max = 150, message = "Title must be at most 150 characters")
    private String title;

    @Size(max = 50, message = "Type must be at most 50 characters")
    private String type;

    private Integer volumeNumber;

    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;
}
