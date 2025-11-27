package com.skilloVilla.Service;

import com.skilloVilla.Dto.LoanDto;
import com.skilloVilla.Dto.LoanRequestDto;
import com.skilloVilla.Entity.Book;
import com.skilloVilla.Entity.Loan;
import com.skilloVilla.Entity.Reader;
import com.skilloVilla.Exception.NotFoundException;
import com.skilloVilla.Repository.BookRepository;
import com.skilloVilla.Repository.LoanRepository;
import com.skilloVilla.Repository.ReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanService {

    private static final int DEFAULT_LOAN_DAYS = 14;

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final ActionLogService actionLogService;

    // Видача книги
    public LoanDto issueBook(LoanRequestDto request) {
        Reader reader = readerRepository.findById(request.getReaderId())
                .orElseThrow(() -> new NotFoundException("Reader not found with id " + request.getReaderId()));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new NotFoundException("Book not found with id " + request.getBookId()));

        if (book.isIssued()) {
            throw new IllegalStateException("Book is already issued");
        }

        int days = request.getDays() != null && request.getDays() > 0
                ? request.getDays()
                : DEFAULT_LOAN_DAYS;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dueDate = now.plusDays(days);

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setReader(reader);
        loan.setIssueDate(now);
        loan.setDueDate(dueDate);
        loan.setReturned(false);

        // оновлюємо книгу
        book.setIssued(true);
        book.setBookIssueDate(now);
        book.setBookReturnDate(dueDate);

        Loan savedLoan = loanRepository.save(loan);
        bookRepository.save(book);

        actionLogService.log("ISSUE_LOAN", "Loan", savedLoan.getLoanId());

        return toDto(savedLoan);
    }


    // Повернення книги
    public LoanDto returnBook(Integer loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new NotFoundException("Loan not found with id " + loanId));

        if (loan.isReturned()) {
            throw new NotFoundException("Loan is already marked as returned");
        }

        LocalDateTime now = LocalDateTime.now();

        loan.setReturned(true);
        loan.setReturnDate(now);

        Book book = loan.getBook();
        book.setIssued(false);
        book.setBookReturnDate(now);

        Loan saved = loanRepository.save(loan);
        bookRepository.save(book);

        actionLogService.log("RETURN_LOAN", "Loan", saved.getLoanId());

        return toDto(saved);
    }


    // Усі операції
    public List<LoanDto> getAll() {
        return loanRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    // Активні (не повернені)
    public List<LoanDto> getActive() {
        return loanRepository.findByReturnedFalse().stream()
                .map(this::toDto)
                .toList();
    }

    // Операції конкретного читача
    public List<LoanDto> getByReader(Integer readerId) {
        return loanRepository.findByReaderReaderId(readerId).stream()
                .map(this::toDto)
                .toList();
    }

    // Операції по конкретній книзі
    public List<LoanDto> getByBook(Integer bookId) {
        return loanRepository.findByBookBookId(bookId).stream()
                .map(this::toDto)
                .toList();
    }

    // ===== mapping =====

    private LoanDto toDto(Loan loan) {
        LoanDto dto = new LoanDto();
        dto.setId(loan.getLoanId());
        dto.setBookId(loan.getBook().getBookId());
        dto.setBookName(loan.getBook().getBookName());
        dto.setReaderId(loan.getReader().getReaderId());
        dto.setReaderName(loan.getReader().getFullName());
        dto.setIssueDate(loan.getIssueDate());
        dto.setDueDate(loan.getDueDate());
        dto.setReturnDate(loan.getReturnDate());
        dto.setReturned(loan.isReturned());
        return dto;
    }

    public List<LoanDto> getRecent(int limit) {
        Pageable pageable = PageRequest.of(
                0,
                limit,
                Sort.by(Sort.Direction.DESC, "issueDate") // або "returnDate" / "createdAt"
        );

        return loanRepository.findAll(pageable)
                .getContent()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
