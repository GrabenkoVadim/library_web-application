package com.skilloVilla.Service;

import com.skilloVilla.Dto.BookDto;
import com.skilloVilla.Entity.Author;
import com.skilloVilla.Entity.Book;
import com.skilloVilla.Dto.AuthorDto;
import com.skilloVilla.Exception.NotFoundException;
import com.skilloVilla.Repository.AuthorRepository;
import com.skilloVilla.Repository.BookRepository;
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

    public List<BookDto> getAll(String name, String author, Boolean issued) {
        Specification<Book> spec = (root, query, cb) -> {
            Predicate p = cb.conjunction();

            if (name != null && !name.isBlank()) {
                p = cb.and(p,
                        cb.like(cb.lower(root.get("bookName")),
                                "%" + name.toLowerCase() + "%"));
            }
            if (author != null && !author.isBlank()) {
                p = cb.and(p,
                        cb.like(cb.lower(root.get("bookAuthor")),
                                "%" + author.toLowerCase() + "%"));
            }
            if (issued != null) {
                if (issued) {
                    p = cb.and(p, cb.isTrue(root.get("isIssued")));
                } else {
                    p = cb.and(p, cb.isFalse(root.get("isIssued")));
                }
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
        if (!bookRepository.existsById(id)) {
            throw new NotFoundException("Book not found with id " + id);
        }
        bookRepository.deleteById(id);

        actionLogService.log("DELETE_BOOK", "Book", id);

    }

    // ====== маппінг Book <-> BookDto ======

    private BookDto toDto(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getBookId());
        dto.setName(book.getBookName());
        dto.setPublisher(book.getBookPublisher());
        dto.setDescription(book.getBookDescription());
        dto.setIssueDate(book.getBookIssueDate());
        dto.setReturnDate(book.getBookReturnDate());
        dto.setIssued(book.isIssued());

        if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
            String names = book.getAuthors().stream()
                    .map(Author::getFullName)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
            dto.setAuthor(names);
        } else {
            dto.setAuthor(book.getBookAuthor());
        }

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
