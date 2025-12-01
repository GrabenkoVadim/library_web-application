package com.skilloVilla.Controller;

import java.util.List;

import com.skilloVilla.Dto.BookDto;
import com.skilloVilla.Dto.AuthorDto;
import com.skilloVilla.Service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    /**
     * GET /api/books
     */
    @GetMapping
    public List<BookDto> getBooks(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Boolean issued,
            @RequestParam(required = false) Integer yearFrom,
            @RequestParam(required = false) Integer yearTo,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String location
    ) {
        return bookService.getAll(name, author, issued, yearFrom, yearTo, isbn, location);
    }


    /**
     * GET /api/books/{id}
     */
    @GetMapping("/{id}")
    public BookDto getBook(@PathVariable Integer id) {
        return bookService.getById(id);
    }

    /**
     * GET /api/books/{id}/authors
     * */
    @GetMapping("/{id}/authors")
    public List<AuthorDto> getBookAuthors(@PathVariable Integer id) {
        return bookService.getAuthorsByBook(id);
    }


    /**
     * POST /api/books
     */
    @PostMapping
    public ResponseEntity<BookDto> createBook(@Valid @RequestBody BookDto dto) {
        BookDto created = bookService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * PUT /api/books/{id}
     */
    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable Integer id, @Valid @RequestBody BookDto dto) {
        return bookService.update(id, dto);
    }

    /**
     * DELETE /api/books/{id}
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Integer id) {
        bookService.delete(id);
    }
}
