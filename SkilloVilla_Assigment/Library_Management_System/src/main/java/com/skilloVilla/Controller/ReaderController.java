package com.skilloVilla.Controller;

import com.skilloVilla.Dto.ReaderDto;
import com.skilloVilla.Service.ReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readers")
public class ReaderController {

    private final ReaderService readerService;

    @GetMapping
    public List<ReaderDto> getReaders() {
        return readerService.getAll();
    }

    @GetMapping("/{id}")
    public ReaderDto getReader(@PathVariable Integer id) {
        return readerService.getById(id);
    }

    @PostMapping
    public ResponseEntity<ReaderDto> createReader(@Valid @RequestBody ReaderDto dto) {
        ReaderDto created = readerService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ReaderDto updateReader(@PathVariable Integer id, @Valid @RequestBody ReaderDto dto) {
        return readerService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReader(@PathVariable Integer id) {
        readerService.delete(id);
    }
}
