package com.skilloVilla.Service;

import com.skilloVilla.Dto.*;
import com.skilloVilla.Entity.Author;
import com.skilloVilla.Entity.Book;
import com.skilloVilla.Exception.NotFoundException;
import com.skilloVilla.Repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public List<AuthorDto> getAll() {
        return authorRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public AuthorDto getById(Integer id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found with id " + id));
        return toDto(author);
    }

    public AuthorDto create(AuthorDto dto) {
        Author author = fromDto(dto);
        author.setAuthorId(null);
        Author saved = authorRepository.save(author);
        return toDto(saved);
    }

    public AuthorDto update(Integer id, AuthorDto dto) {
        Author existing = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found with id " + id));

        existing.setFullName(dto.getFullName());
        existing.setBiography(dto.getBiography());

        Author saved = authorRepository.save(existing);
        return toDto(saved);
    }

    public void delete(Integer id) {
        if (!authorRepository.existsById(id)) {
            throw new NotFoundException("Author not found with id " + id);
        }
        authorRepository.deleteById(id);
    }

    // всі книги цього автора (повертаємо BookDto)
    public List<BookDto> getBooksByAuthor(Integer authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Author not found with id " + authorId));

        return author.getBooks().stream()
                .map(this::bookToDto)
                .toList();
    }

    // ===== мапінг Author <-> AuthorDto =====

    private AuthorDto toDto(Author author) {
        AuthorDto dto = new AuthorDto();
        dto.setId(author.getAuthorId());
        dto.setFullName(author.getFullName());
        dto.setBiography(author.getBiography());
        return dto;
    }

    private Author fromDto(AuthorDto dto) {
        Author author = new Author();
        author.setAuthorId(dto.getId());
        author.setFullName(dto.getFullName());
        author.setBiography(dto.getBiography());
        return author;
    }

    // ===== мапінг Book -> BookDto (узгоджений з тим, що ти вже робиш у BookService) =====

    private BookDto bookToDto(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getBookId());
        dto.setName(book.getBookName());
        dto.setAuthor(book.getBookAuthor());
        dto.setPublisher(book.getBookPublisher());
        dto.setDescription(book.getBookDescription());
        dto.setIssueDate(book.getBookIssueDate());
        dto.setReturnDate(book.getBookReturnDate());
        dto.setIssued(book.isIssued());
        return dto;
    }

    // Пошук авторів по імені разом із їхніми книгами
    public List<AuthorWithBooksDto> searchWithBooks(String namePart) {
        List<Author> authors;

        if (namePart == null || namePart.isBlank()) {
            authors = authorRepository.findAll();
        } else {
            authors = authorRepository.findByFullNameContainingIgnoreCase(namePart);
        }

        return authors.stream()
                .map(this::toWithBooksDto)
                .toList();
    }

    private AuthorWithBooksDto toWithBooksDto(Author author) {
        AuthorWithBooksDto dto = new AuthorWithBooksDto();
        dto.setId(author.getAuthorId());
        dto.setFullName(author.getFullName());
        dto.setBiography(author.getBiography());

        if (author.getBooks() != null) {
            List<BookShortDto> books = author.getBooks().stream()
                    .map(book -> {
                        BookShortDto b = new BookShortDto();
                        b.setId(book.getBookId());
                        b.setName(book.getBookName());
                        return b;
                    })
                    .toList();
            dto.setBooks(books);
        }

        return dto;
    }

    public List<AuthorStatsDto> getAuthorStats() {
        return authorRepository.findAll().stream()
                .map(this::toStatsDto)
                .toList();
    }

    private AuthorStatsDto toStatsDto(Author author) {
        AuthorStatsDto dto = new AuthorStatsDto();
        dto.setId(author.getAuthorId());
        dto.setFullName(author.getFullName());

        long count = 0L;
        if (author.getBooks() != null) {
            count = author.getBooks().size();
        }
        dto.setBooksCount(count);

        return dto;
    }
}
