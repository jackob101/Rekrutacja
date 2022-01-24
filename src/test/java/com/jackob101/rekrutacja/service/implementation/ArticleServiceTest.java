package com.jackob101.rekrutacja.service.implementation;

import com.jackob101.rekrutacja.model.Article;
import com.jackob101.rekrutacja.model.ArticleContent;
import com.jackob101.rekrutacja.model.Author;
import com.jackob101.rekrutacja.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    ArticleRepository articleRepository;

    ArticleService articleService;

    Article testArticle;


    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        Author author = new Author("Tom", "Mot");
        ArticleContent articleContent = new ArticleContent("This is article title", "This is article body " + randomAlphabetic(450));

        testArticle = new Article(articleContent, author, LocalDate.now().plusDays(5), "This is magazine name");

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        articleService = new ArticleService(articleRepository, validator);
    }

    @Test
    void create_article_successfully() {

        doAnswer(returnsFirstArg()).when(articleRepository).save(testArticle);

        Article saved = articleService.create(testArticle);

        assertNotNull(saved);
        assertNotNull(saved.getArticleContent());
        assertNotNull(saved.getAuthor());
        assertEquals(testArticle.getArticleContent().getBody(), saved.getArticleContent().getBody());
        assertEquals(testArticle.getArticleContent().getTitle(), saved.getArticleContent().getTitle());
        assertEquals(testArticle.getMagazineName(), saved.getMagazineName());

    }

    @Test
    void create_article_validationError() {

        testArticle.setArticleContent(null);
        testArticle.setAuthor(null);

        assertThrows(RuntimeException.class, () -> articleService.create(testArticle));
    }


    @Test
    void update_article_successfully() {

        ArticleContent articleContent = new ArticleContent("This is article title",
                "The body of article was changed");
        testArticle.setArticleContent(articleContent);
        testArticle.setId(1L);

        doAnswer(returnsFirstArg()).when(articleRepository).save(testArticle);
        doReturn(true).when(articleRepository).existsById(anyLong());

        Article saved = articleService.update(testArticle);

        assertNotNull(saved);
        assertNotNull(saved.getArticleContent());
        assertNotNull(saved.getAuthor());
        assertEquals(testArticle.getArticleContent().getBody(), saved.getArticleContent().getBody());
        assertEquals(testArticle.getArticleContent().getTitle(), saved.getArticleContent().getTitle());
        assertEquals(testArticle.getAuthor().getName(), saved.getAuthor().getName());
        assertEquals(testArticle.getMagazineName(), saved.getMagazineName());

    }

    @Test
    void update_articleId_null() {

        testArticle.setId(null);

        assertThrows(RuntimeException.class, () -> articleService.update(testArticle));

    }

    @Test
    void update_article_validationError() {

        testArticle.setId(1L);

        testArticle.setArticleContent(null);
        testArticle.setAuthor(null);
        testArticle.setMagazineName(null);

        assertThrows(RuntimeException.class, () -> articleService.update(testArticle));
    }

    @Test
    void find_id_null() {
        assertThrows(RuntimeException.class, () -> articleService.find(null));
    }

    @Test
    void find_id_successfully() {

        doReturn(Optional.of(testArticle)).when(articleRepository).findById(anyLong());

        Article article = articleService.find(1L);

        assertNotNull(article);
        assertEquals(testArticle.getMagazineName(), article.getMagazineName());
        assertNotNull(article.getAuthor());
        assertNotNull(article.getArticleContent());
    }

    @Test
    void find_id_notFound() {

        doReturn(Optional.empty()).when(articleRepository).findById(anyLong());

        assertThrows(RuntimeException.class, () -> articleService.find(1L));
    }

    @Test
    void delete_id_null() {
        assertThrows(RuntimeException.class, () -> articleService.find(null));
    }

    @Test
    void delete_id_successfully() {

        doReturn(true, false).when(articleRepository).existsById(anyLong());

        assertDoesNotThrow(() -> articleService.delete(1L));
    }

    @Test
    void delete_id_failed() {

        doReturn(true).when(articleRepository).existsById(anyLong());

        assertThrows(RuntimeException.class, () -> articleService.delete(1L));
    }

    @Test
    void delete_id_notFound() {

        doReturn(false).when(articleRepository).existsById(anyLong());

        assertThrows(RuntimeException.class, () -> articleService.delete(1L));
    }

    @ParameterizedTest
    @CsvSource(value = {"NULL", "''"}, nullValues = "NULL")
    void findByKeyword_keyword_incorrectValue(String keyword) {
        assertThrows(RuntimeException.class,() -> articleService.findByKeyword(keyword));
    }

    @Test
    void findByKeyword_keyword_successfully() {

        doReturn(List.of(testArticle)).when(articleRepository).findByKeyword("title");

        List<Article> articleList = articleService.findByKeyword("title");

        assertNotNull(articleList);
        assertEquals(1,articleList.size());
        assertEquals(testArticle.getArticleContent().getTitle(),articleList.get(0).getArticleContent().getTitle());
    }
}