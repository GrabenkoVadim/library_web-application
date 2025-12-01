package com.skilloVilla.Service;

import com.skilloVilla.Dto.BookCollectionDto;
import com.skilloVilla.Dto.BookDto;
import com.skilloVilla.Entity.Author;
import com.skilloVilla.Entity.Book;
import com.skilloVilla.Dto.AuthorDto;
import com.skilloVilla.Entity.BookCollection;
import com.skilloVilla.Entity.Loan;
import com.skilloVilla.Exception.NotFoundException;
import com.skilloVilla.Repository.AuthorRepository;
import com.skilloVilla.Repository.BookRepository;
import com.skilloVilla.Repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final ActionLogService actionLogService;
    private final LoanRepository loanRepository;

    public List<BookDto> getAll(
            String name,
            String author,
            Boolean issued,
            Integer yearFrom,
            Integer yearTo,
            String isbn,
            String location
    ) {
        Specification<Book> spec = (root, query, cb) -> {
            Predicate p = cb.conjunction();

            if (name != null && !name.isBlank()) {
                p = cb.and(
                        p,
                        cb.like(
                                cb.lower(root.get("bookName")),
                                "%" + name.toLowerCase() + "%"
                        )
                );
            }

            if (author != null && !author.isBlank()) {
                p = cb.and(
                        p,
                        cb.like(
                                cb.lower(root.get("bookAuthor")),
                                "%" + author.toLowerCase() + "%"
                        )
                );
            }

            if (issued != null) {
                if (issued) {
                    p = cb.and(p, cb.isTrue(root.get("isIssued")));
                } else {
                    p = cb.and(p, cb.isFalse(root.get("isIssued")));
                }
            }

            if (yearFrom != null) {
                p = cb.and(
                        p,
                        cb.greaterThanOrEqualTo(root.get("publicationYear"), yearFrom)
                );
            }

            if (yearTo != null) {
                p = cb.and(
                        p,
                        cb.lessThanOrEqualTo(root.get("publicationYear"), yearTo)
                );
            }

            if (isbn != null && !isbn.isBlank()) {
                p = cb.and(
                        p,
                        cb.like(
                                cb.lower(root.get("isbn")),
                                "%" + isbn.toLowerCase() + "%"
                        )
                );
            }

            if (location != null && !location.isBlank()) {
                p = cb.and(
                        p,
                        cb.like(
                                cb.lower(root.get("location")),
                                "%" + location.toLowerCase() + "%"
                        )
                );
            }

            return p;
        };

        return bookRepository.findAll(spec).stream()
                .map(this::toDto)
                .toList();
    }

    public BookDto getById(Integer id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found with id " + id));
        return toDto(book);
    }

    public BookDto create(BookDto dto) {
        Book book = fromDto(dto);
        book.setBookId(null);

        book.setBookAuthor(dto.getAuthor());

        List<Author> authors = resolveAuthorsFromString(dto.getAuthor());
        book.setAuthors(authors);

        Book saved = bookRepository.save(book);

        actionLogService.log("CREATE_BOOK", "Book", saved.getBookId());

        return toDto(saved);
    }


    public BookDto update(Integer id, BookDto dto) {
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found with id " + id));

        existing.setBookName(dto.getName());
        existing.setBookAuthor(dto.getAuthor());
        existing.setBookPublisher(dto.getPublisher());
        existing.setBookDescription(dto.getDescription());
        existing.setBookIssueDate(dto.getIssueDate());
        existing.setBookReturnDate(dto.getReturnDate());
        existing.setIssued(dto.isIssued());

        List<Author> authors = resolveAuthorsFromString(dto.getAuthor());
        existing.setAuthors(authors);

        Book saved = bookRepository.save(existing);

        actionLogService.log("UPDATE_BOOK", "Book", saved.getBookId());

        return toDto(saved);
    }


    public void delete(Integer id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found with id " + id));

        var loans = loanRepository.findByBookBookId(id);

        loans.forEach(loan -> loan.setBook(null));
        loanRepository.saveAll(loans);

        bookRepository.delete(book);

        actionLogService.log("DELETE_BOOK", "Book", id);
    }

    private BookDto toDto(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getBookId());
        dto.setName(book.getBookName());
        dto.setPublisher(book.getBookPublisher());
        dto.setDescription(book.getBookDescription());
        dto.setIssueDate(book.getBookIssueDate());
        dto.setReturnDate(book.getBookReturnDate());
        dto.setIssued(book.isIssued());

        // 🔹 рік / ISBN / розташування
        dto.setYear(book.getPublicationYear());
        dto.setIsbn(book.getIsbn());
        dto.setLocation(book.getLocation());

        // 🔹 поточна активна позика (як і було)
        loanRepository
                .findFirstByBookBookIdAndReturnedFalseOrderByIssueDateDesc(book.getBookId())
                .ifPresent(loan -> {
                    dto.setIssueDate(loan.getIssueDate());
                    dto.setDueDate(loan.getDueDate());
                    dto.setReturnDate(loan.getReturnDate());
                });

        // 🔹 автор(и)
        if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
            String names = book.getAuthors().stream()
                    .map(Author::getFullName)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
            dto.setAuthor(names);
        } else {
            dto.setAuthor(book.getBookAuthor());
        }

        // 🔹 колекції / збірники / томи
        if (book.getCollections() != null && !book.getCollections().isEmpty()) {
            var colDtos = book.getCollections().stream()
                    .map(col -> {
                        BookCollectionDto cd = new BookCollectionDto();
                        cd.setId(col.getCollectionId());
                        cd.setTitle(col.getTitle());
                        cd.setType(col.getType());
                        cd.setVolumeNumber(col.getVolumeNumber());
                        cd.setDescription(col.getDescription());
                        return cd;
                    })
                    .toList();
            dto.setCollections(colDtos);
        } else {
            dto.setCollections(List.of());
        }

        return dto;
    }

    private BookCollectionDto collectionToDto(BookCollection col) {
        BookCollectionDto dto = new BookCollectionDto();
        dto.setId(col.getCollectionId());
        dto.setTitle(col.getTitle());
        dto.setType(col.getType());
        dto.setVolumeNumber(col.getVolumeNumber());
        dto.setDescription(col.getDescription());
        return dto;
    }

    private Book fromDto(BookDto dto) {
        Book book = new Book();
        book.setBookId(dto.getId());
        book.setBookName(dto.getName());
        book.setBookAuthor(dto.getAuthor());
        book.setBookPublisher(dto.getPublisher());
        book.setBookDescription(dto.getDescription());
        book.setBookIssueDate(dto.getIssueDate());
        book.setBookReturnDate(dto.getReturnDate());
        book.setIssued(dto.isIssued());

        book.setPublicationYear(dto.getYear());
        book.setIsbn(dto.getIsbn());
        book.setLocation(dto.getLocation());

        // collections тут поки не чіпаємо — ними керує BookCollectionService
        return book;
    }

    private List<Author> resolveAuthorsFromString(String authorField) {
        List<Author> result = new java.util.ArrayList<>();

        if (authorField == null || authorField.isBlank()) {
            return result;
        }

        String[] names = authorField.split(",");
        for (String rawName : names) {
            String name = rawName.trim();
            if (name.isEmpty()) continue;

            Author author = authorRepository.findAll().stream()
                    .filter(a -> a.getFullName().equalsIgnoreCase(name))
                    .findFirst()
                    .orElseGet(() -> {
                        Author newAuthor = new Author();
                        newAuthor.setFullName(name);
                        return authorRepository.save(newAuthor);
                    });

            result.add(author);
        }

        return result;
    }

    // ====== Автори книги ======
    public List<AuthorDto> getAuthorsByBook(Integer bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found with id " + bookId));

        if (book.getAuthors() == null) {
            return List.of();
        }

        return book.getAuthors().stream()
                .map(this::authorToDto)
                .toList();
    }

    private AuthorDto authorToDto(Author author) {
        AuthorDto dto = new AuthorDto();
        dto.setId(author.getAuthorId());
        dto.setFullName(author.getFullName());
        dto.setBiography(author.getBiography());
        return dto;
    }
}
