package com.jackob101.rekrutacja.model;


import com.jackob101.rekrutacja.Constants;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Article {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    private ArticleContent articleContent;

    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "AUTHOR_NAME", nullable = false)),
            @AttributeOverride(name = "lastName", column = @Column(name = "AUTHOR_LAST_NAME", nullable = false))
    })
    private Author author;

    @Column(nullable = false)
    private LocalDate publicationDate;

    @Column(nullable = false)
    private String magazineName;

    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    private Timestamp savedTimestamp;

}
