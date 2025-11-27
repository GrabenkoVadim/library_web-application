package com.skilloVilla.Dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class BookDto {
    private Integer id;

    @NotBlank(message = "Book name is required")
    @Size(max = 100, message = "Book name must be at most 100 characters")
    private String name;

    @NotBlank(message = "Author field is required")
    @Size(max = 200, message = "Author field must be at most 200 characters")
    private String author;

    @NotBlank(message = "Publisher is required")
    @Size(max = 500, message = "Publisher must be at most 500 characters")
    private String publisher;

    @Size(max = 2500, message = "Description must be at most 2500 characters")
    private String description;

    private LocalDateTime issueDate;
    private LocalDateTime returnDate;
    private boolean issued;
}
