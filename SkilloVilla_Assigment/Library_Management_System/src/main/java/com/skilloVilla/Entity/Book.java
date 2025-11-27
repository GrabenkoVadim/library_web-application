package com.skilloVilla.Entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bookId;
	
	@Column(length = 100, nullable = false)
	private String bookName;
	
	@Column(length = 100, nullable = false)
	private String bookAuthor;
	
	@Column(length = 500, nullable = false)
	private String bookPublisher;
	
    @Column(length = 2500)
	private String bookDescription;
    
    private LocalDateTime bookIssueDate;
    
    private LocalDateTime bookReturnDate;
    
    private boolean isIssued = false;
    
    @ManyToOne
    private User user;

    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors = new ArrayList<>();

    @ManyToMany(mappedBy = "books")
    @JsonIgnore
    private List<BookCollection> collections = new ArrayList<>();


}
