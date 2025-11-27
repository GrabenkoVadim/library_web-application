package com.skilloVilla.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer loanId;

    @ManyToOne(optional = false)
    private Book book;

    @ManyToOne(optional = false)
    private Reader reader;

    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    private boolean returned = false;
}
