package com.jackob101.rekrutacja.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.validation.Validation;
import java.time.LocalDate;
import java.util.HashMap;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ArticleTest {

    Article testArticle;

    SmartValidator validator;

    MapBindingResult errors;

    @BeforeEach
    void setUp() {

        Author author = new Author("Tom", "Mot");
        ArticleContent articleContent = new ArticleContent("This is article title", "This is article body " + randomAlphabetic(450));

        testArticle = new Article(articleContent, author, LocalDate.now().plusDays(5), "This is magazine name");

        validator = new SpringValidatorAdapter(Validation.buildDefaultValidatorFactory().getValidator());

        errors = new MapBindingResult(new HashMap<>(), "Article");
    }

    @ParameterizedTest
    @CsvSource(value = {"'','', 2", "NULL,'', 2", "'',NULL, 2", "valid,'',1", "NULL,valid, 1"}, nullValues = "NULL")
    void articleContentFields_validationError(String title, String body, int errorCount) {
        ArticleContent articleContent = new ArticleContent(title, body);
        testArticle.setArticleContent(articleContent);

        validator.validate(testArticle, errors);

        assertTrue(errors.hasErrors());
        assertEquals(errorCount,errors.getErrorCount());
    }

    @ParameterizedTest
    @CsvSource(value = {"'','', 2", "NULL,'', 2", "'',NULL, 2", "valid,'', 1", "NULL,valid, 1"}, nullValues = "NULL")
    void articleAuthorFields_validationError(String name, String lastName, int errorCount) {
        Author author = new Author(name, lastName);
        testArticle.setAuthor(author);

        validator.validate(testArticle,errors);

        assertTrue(errors.hasErrors());
        assertEquals(errorCount,errors.getErrorCount());
    }

    @ParameterizedTest
    @CsvSource(value = {"''", "NULL"}, nullValues = "NULL")
    void articleMagazineName_validationError(String magazineName) {

        testArticle.setMagazineName(magazineName);

        validator.validate(testArticle,errors);

        assertTrue(errors.hasErrors());
        assertEquals(1,errors.getErrorCount());
    }

    @Test
    void articleContentBody_tooLarge() {

        ArticleContent content = new ArticleContent("Title", randomAlphabetic(10001));
        testArticle.setArticleContent(content);

        validator.validate(testArticle,errors);

        assertTrue(errors.hasErrors());
        assertEquals(1,errors.getErrorCount());
    }

    @Test
    void articleContentTitle_tooLarge() {

        ArticleContent content = new ArticleContent(randomAlphabetic(600)," This is body");
        testArticle.setArticleContent(content);

        validator.validate(testArticle,errors);

        assertTrue(errors.hasErrors());
        assertEquals(1,errors.getErrorCount());
    }

    @Test
    void articleMagazineName_tooLarge() {

        testArticle.setMagazineName(randomAlphabetic(700));

        validator.validate(testArticle,errors);

        assertTrue(errors.hasErrors());
        assertEquals(1,errors.getErrorCount());
    }
}