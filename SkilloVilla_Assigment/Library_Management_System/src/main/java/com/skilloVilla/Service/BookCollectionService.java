package com.skilloVilla.Service;

import com.skilloVilla.Dto.BookCollectionDto;
import com.skilloVilla.Dto.BookDto;
import com.skilloVilla.Entity.Book;
import com.skilloVilla.Entity.BookCollection;
import com.skilloVilla.Exception.NotFoundException;
import com.skilloVilla.Repository.BookCollectionRepository;
import com.skilloVilla.Repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookCollectionService {

    private final BookCollectionRepository collectionRepository;
    private final BookRepository bookRepository;

    public List<BookCollectionDto> getAll() {
        return collectionRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public BookCollectionDto getById(Integer id) {
        BookCollection col = collectionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Collection not found with id " + id));
        return toDto(col);
    }

    public BookCollectionDto create(BookCollectionDto dto) {
        BookCollection col = fromDto(dto);
        col.setCollectionId(null);
        BookCollection saved = collectionRepository.save(col);
        return toDto(saved);
    }

    public BookCollectionDto update(Integer id, BookCollectionDto dto) {
        BookCollection existing = collectionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Collection not found with id " + id));

        existing.setTitle(dto.getTitle());
        existing.setType(dto.getType());
        existing.setVolumeNumber(dto.getVolumeNumber());
        existing.setDescription(dto.getDescription());

        BookCollection saved = collectionRepository.save(existing);
        return toDto(saved);
    }

    public void delete(Integer id) {
        if (!collectionRepository.existsById(id)) {
            throw new NotFoundException("Collection not found with id " + id);
        }
        collectionRepository.deleteById(id);
    }

    // ===== Книги в збірнику =====

    public List<BookDto> getBooksInCollection(Integer collectionId) {
        BookCollection col = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new NotFoundException("Collection not found with id " + collectionId));

        return col.getBooks().stream()
                .map(this::bookToDto)
                .toList();
    }

    // Оновити список книг у збірнику за списком bookId
    public List<BookDto> setBooksInCollection(Integer collectionId, List<Integer> bookIds) {
        BookCollection col = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new NotFoundException("Collection not found with id " + collectionId));

        List<Book> books = new ArrayList<>();
        for (Integer bookId : bookIds) {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new NotFoundException("Book not found with id " + bookId));
            books.add(book);
        }

        col.setBooks(books);
        BookCollection saved = collectionRepository.save(col);

        return saved.getBooks().stream()
                .map(this::bookToDto)
                .toList();
    }

    // ===== mapping =====

    private BookCollectionDto toDto(BookCollection col) {
        BookCollectionDto dto = new BookCollectionDto();
        dto.setId(col.getCollectionId());
        dto.setTitle(col.getTitle());
        dto.setType(col.getType());
        dto.setVolumeNumber(col.getVolumeNumber());
        dto.setDescription(col.getDescription());
        return dto;
    }

    private BookCollection fromDto(BookCollectionDto dto) {
        BookCollection col = new BookCollection();
        col.setCollectionId(dto.getId());
        col.setTitle(dto.getTitle());
        col.setType(dto.getType());
        col.setVolumeNumber(dto.getVolumeNumber());
        col.setDescription(dto.getDescription());
        return col;
    }

    // маппінг Book -> BookDto (той самий формат, що в BookService)
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
}
