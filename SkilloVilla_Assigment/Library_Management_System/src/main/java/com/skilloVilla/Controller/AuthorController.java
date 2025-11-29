package com.skilloVilla.Controller;

import com.skilloVilla.Dto.AuthorDto;
import com.skilloVilla.Dto.BookDto;
import com.skilloVilla.Dto.AuthorWithBooksDto;
import com.skilloVilla.Service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    // GET /api/authors
    @GetMapping
    public List<AuthorDto> getAuthors() {
        return authorService.getAll();
    }

    // GET /api/authors/{id}
    @GetMapping("/{id}")
    public AuthorDto getAuthor(@PathVariable Integer id) {
        return authorService.getById(id);
    }

    // GET /api/authors/search?name=...
    @GetMapping("/search")
    public List<AuthorWithBooksDto> searchAuthorsWithBooks(
            @RequestParam(required = false) String name
    ) {
        return authorService.searchWithBooks(name);
    }


    // POST /api/authors
    @PostMapping
    public ResponseEntity<AuthorDto> createAuthor(@Valid @RequestBody AuthorDto dto) {
        AuthorDto created = authorService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // PUT /api/authors/{id}
    @PutMapping("/{id}")
    public AuthorDto updateAuthor(@PathVariable Integer id, @Valid @RequestBody AuthorDto dto) {
        return authorService.update(id, dto);
    }

    // DELETE /api/authors/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAuthor(@PathVariable Integer id) {
        authorService.delete(id);
    }

    // GET /api/authors/{id}/books
    @GetMapping("/{id}/books")
    public List<BookDto> getAuthorBooks(@PathVariable Integer id) {
        return authorService.getBooksByAuthor(id);
    }
}
