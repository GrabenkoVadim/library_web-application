package com.skilloVilla.Repository;

import com.skilloVilla.Entity.BookCollection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookCollectionRepository extends JpaRepository<BookCollection, Integer> {
}
