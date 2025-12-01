package com.skilloVilla.Dto;

import lombok.Data;

@Data
public class AuthorStatsDto {
    private Integer id;
    private String fullName;
    private Long booksCount;
}
