package com.jackob101.rekrutacja.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Immutable
@Getter
@AllArgsConstructor
public class ArticleContent {

    @Column(nullable = false)
    private final String title;

    @Column(nullable = false)
    private final String body;

}
