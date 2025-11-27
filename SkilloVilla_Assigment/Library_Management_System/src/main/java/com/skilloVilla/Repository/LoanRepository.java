package com.skilloVilla.Repository;

import com.skilloVilla.Entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Integer> {

    List<Loan> findByReaderReaderId(Integer readerId);

    List<Loan> findByBookBookId(Integer bookId);

    List<Loan> findByReturnedFalse(); // активні (ще не повернені)

    Page<Loan> findAll(Pageable pageable);

}
