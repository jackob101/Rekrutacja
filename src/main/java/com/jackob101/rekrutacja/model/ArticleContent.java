package com.jackob101.rekrutacja.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Embeddable
@Immutable
@Getter
@AllArgsConstructor
public class ArticleContent {

    @Size(max = 500)
    @NotBlank(message = "Article content title cannot be blank or null")
    @Column(nullable = false)
    private String title;

    @Size(max = 10000)
    @NotBlank(message = "Article content body cannot be blank or null")
    @Column(nullable = false)
    private String body;

    protected ArticleContent(){}

}
