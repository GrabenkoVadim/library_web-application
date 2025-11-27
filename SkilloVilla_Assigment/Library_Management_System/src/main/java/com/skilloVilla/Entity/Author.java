package com.skilloVilla.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer authorId;

    @Column(length = 150, nullable = false)
    private String fullName;

    @Column(length = 1000)
    private String biography;

    @ManyToMany(mappedBy = "authors")
    @JsonIgnore
    private List<Book> books = new ArrayList<>();
}
