package com.jackob101.rekrutacja.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@Embeddable
public class Author {

    @Size(max = 200)
    @NotBlank(message = "Author name cannot be blank or null")
    @Column(nullable = false)
    private String name;

    @Size(max = 200)
    @NotBlank(message = "Author last name cannot be blank or null")
    @Column(nullable = false)
    private String lastName;
}
