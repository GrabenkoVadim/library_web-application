package com.skilloVilla.Service;

import com.skilloVilla.Dto.ReaderDto;
import com.skilloVilla.Entity.Reader;
import com.skilloVilla.Exception.NotFoundException;
import com.skilloVilla.Repository.ReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReaderService {

    private final ReaderRepository readerRepository;

    public List<ReaderDto> getAll() {
        return readerRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public ReaderDto getById(Integer id) {
        Reader reader = readerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reader not found with id " + id));
        return toDto(reader);
    }

    public ReaderDto create(ReaderDto dto) {
        Reader reader = fromDto(dto);
        reader.setReaderId(null); // новий читач
        Reader saved = readerRepository.save(reader);
        return toDto(saved);
    }

    public ReaderDto update(Integer id, ReaderDto dto) {
        Reader existing = readerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reader not found with id " + id));

        existing.setFullName(dto.getFullName());
        existing.setEmail(dto.getEmail());
        existing.setPhone(dto.getPhone());
        existing.setAddress(dto.getAddress());

        Reader saved = readerRepository.save(existing);
        return toDto(saved);
    }

    public void delete(Integer id) {
        if (!readerRepository.existsById(id)) {
            throw new NotFoundException("Reader not found with id " + id);
        }

        readerRepository.deleteById(id);
    }

    // ===== mapping =====

    private ReaderDto toDto(Reader r) {
        ReaderDto dto = new ReaderDto();
        dto.setId(r.getReaderId());
        dto.setFullName(r.getFullName());
        dto.setEmail(r.getEmail());
        dto.setPhone(r.getPhone());
        dto.setAddress(r.getAddress());
        return dto;
    }

    private Reader fromDto(ReaderDto dto) {
        Reader r = new Reader();
        r.setReaderId(dto.getId());
        r.setFullName(dto.getFullName());
        r.setEmail(dto.getEmail());
        r.setPhone(dto.getPhone());
        r.setAddress(dto.getAddress());
        return r;
    }
}
