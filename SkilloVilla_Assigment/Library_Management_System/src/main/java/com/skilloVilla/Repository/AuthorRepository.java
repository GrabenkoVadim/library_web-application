package com.skilloVilla.Repository;

import com.skilloVilla.Entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author,Integer> {
    List<Author> findByFullNameContainingIgnoreCase(String fullName);
}
