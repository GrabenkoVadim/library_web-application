package com.skilloVilla.Controller;

import com.skilloVilla.Dto.LoanDto;
import com.skilloVilla.Dto.LoanRequestDto;
import com.skilloVilla.Service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    // POST /api/loans
    @PostMapping
    public ResponseEntity<LoanDto> issueBook(@Valid @RequestBody LoanRequestDto request) {
        LoanDto dto = loanService.issueBook(request);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    // POST /api/loans/{id}/return
    @PostMapping("/{id}/return")
    public LoanDto returnBook(@PathVariable Integer id) {
        return loanService.returnBook(id);
    }

    // GET /api/loans
    @GetMapping
    public List<LoanDto> getAll() {
        return loanService.getAll();
    }

    // GET /api/loans/active
    @GetMapping("/active")
    public List<LoanDto> getActive() {
        return loanService.getActive();
    }

    // GET /api/loans/by-reader/{readerId}
    @GetMapping("/by-reader/{readerId}")
    public List<LoanDto> getByReader(@PathVariable Integer readerId) {
        return loanService.getByReader(readerId);
    }

    // GET /api/loans/by-book/{bookId}
    @GetMapping("/by-book/{bookId}")
    public List<LoanDto> getByBook(@PathVariable Integer bookId) {
        return loanService.getByBook(bookId);
    }

    // GET /api/loans/recent?limit=10
    @GetMapping("/recent")
    public List<LoanDto> getRecent(@RequestParam(defaultValue = "10") Integer limit) {
        return loanService.getRecent(limit);
    }
}
