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

    @Size(max = 200, message = "Author name max size is 200 chars")
    @NotBlank(message = "Author name cannot be blank or null")
    @Column(nullable = false)
    private String name;

    @Size(max = 200, message = "Author last name max size is 200 chars")
    @NotBlank(message = "Author last name cannot be blank or null")
    @Column(nullable = false)
    private String lastName;

    protected Author(){}
}
