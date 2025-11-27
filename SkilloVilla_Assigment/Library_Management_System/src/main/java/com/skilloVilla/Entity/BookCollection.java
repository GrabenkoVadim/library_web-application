package com.skilloVilla.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class BookCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer collectionId;

    @Column(length = 150, nullable = false)
    private String title;

    @Column(length = 50)
    private String type;

    private Integer volumeNumber;

    @Column(length = 1000)
    private String description;

    @ManyToMany
    @JoinTable(
            name = "collection_book",
            joinColumns = @JoinColumn(name = "collection_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Book> books = new ArrayList<>();
}
