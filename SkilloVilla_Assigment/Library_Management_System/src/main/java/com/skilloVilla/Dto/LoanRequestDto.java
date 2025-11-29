package com.skilloVilla.Dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class LoanRequestDto {

    @NotNull(message = "Book id is required")
    private Integer bookId;

    @NotNull(message = "Reader id is required")
    private Integer readerId;

    @Min(value = 1, message = "Days must be at least 1")
    private Integer days;
}
