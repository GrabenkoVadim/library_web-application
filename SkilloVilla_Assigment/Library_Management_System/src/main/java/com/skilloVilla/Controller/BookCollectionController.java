package com.skilloVilla.Controller;

import com.skilloVilla.Dto.BookCollectionDto;
import com.skilloVilla.Dto.BookDto;
import com.skilloVilla.Service.BookCollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/collections")
public class BookCollectionController {

    private final BookCollectionService collectionService;

    // GET /api/collections
    @GetMapping
    public List<BookCollectionDto> getCollections() {
        return collectionService.getAll();
    }

    // GET /api/collections/{id}
    @GetMapping("/{id}")
    public BookCollectionDto getCollection(@PathVariable Integer id) {
        return collectionService.getById(id);
    }

    // POST /api/collections
    @PostMapping
    public ResponseEntity<BookCollectionDto> createCollection(@Valid @RequestBody BookCollectionDto dto) {
        BookCollectionDto created = collectionService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // PUT /api/collections/{id}
    @PutMapping("/{id}")
    public BookCollectionDto updateCollection(@PathVariable Integer id, @Valid @RequestBody BookCollectionDto dto) {
        return collectionService.update(id, dto);
    }

    // DELETE /api/collections/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCollection(@PathVariable Integer id) {
        collectionService.delete(id);
    }

    // GET /api/collections/{id}/books
    @GetMapping("/{id}/books")
    public List<BookDto> getBooksInCollection(@PathVariable Integer id) {
        return collectionService.getBooksInCollection(id);
    }

    // PUT /api/collections/{id}/books
    // Body: [1,2,3]
    @PutMapping("/{id}/books")
    public List<BookDto> setBooksInCollection(
            @PathVariable Integer id,
            @RequestBody List<Integer> bookIds
    ) {
        return collectionService.setBooksInCollection(id, bookIds);
    }
}
