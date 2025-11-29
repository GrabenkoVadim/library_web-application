package com.skilloVilla.Dto;

import lombok.Data;

import java.util.List;

@Data
public class AuthorWithBooksDto {
    private Integer id;
    private String fullName;
    private String biography;
    private List<BookShortDto> books;
}
