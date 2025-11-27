package com.skilloVilla.Dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanDto {
    private Integer id;
    private Integer bookId;
    private String bookName;
    private Integer readerId;
    private String readerName;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private boolean returned;
}
