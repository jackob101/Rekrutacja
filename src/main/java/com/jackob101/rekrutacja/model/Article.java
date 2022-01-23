package com.jackob101.rekrutacja.model;


import com.jackob101.rekrutacja.Constants;
import com.jackob101.rekrutacja.validation.groups.OnUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@Entity
public class Article {

    @NotNull(message = "Id cannot be null", groups = OnUpdate.class)
    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @Valid
    @NotNull(message = "Article content cannot be null")
    private ArticleContent articleContent;

    @Valid
    @NotNull(message = "Author cannot be null")
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "AUTHOR_NAME", nullable = false)),
            @AttributeOverride(name = "lastName", column = @Column(name = "AUTHOR_LAST_NAME", nullable = false))
    })
    private Author author;

    @NotNull(message = "Publication date cannot be null")
    @Column(nullable = false)
    private LocalDate publicationDate;


    @Size(max = 500)
    @NotBlank(message = "Magazine name cannot be null or blank")
    @Column(nullable = false)
    private String magazineName;

    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    private Timestamp savedTimestamp;

    public Article(ArticleContent articleContent, Author author, LocalDate publicationDate, String magazineName) {
        this.articleContent = articleContent;
        this.author = author;
        this.publicationDate = publicationDate;
        this.magazineName = magazineName;
    }
}
