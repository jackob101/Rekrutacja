package com.jackob101.rekrutacja.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Author {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastName;
}
