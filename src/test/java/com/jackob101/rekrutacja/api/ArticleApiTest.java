package com.jackob101.rekrutacja.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jackob101.rekrutacja.exception.StatusException;
import com.jackob101.rekrutacja.model.Article;
import com.jackob101.rekrutacja.model.ArticleContent;
import com.jackob101.rekrutacja.model.Author;
import com.jackob101.rekrutacja.service.definition.IArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ArticleApi.class)
class ArticleApiTest {

    @MockBean
    IArticleService iArticleService;

    @Autowired
    MockMvc mockMvc;

    Article testArticle;

    ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {

        Author author = new Author("Tom", "Mot");
        ArticleContent articleContent = new ArticleContent("This is article title", "This is article body " + randomAlphabetic(450));
        testArticle = new Article(articleContent, author, LocalDate.now().plusDays(5), "This is magazine name");

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

    }

    @Test
    void create_article_successfully() throws Exception {

        String content = objectMapper.writeValueAsString(testArticle);

        doAnswer(returnsFirstArg()).when(iArticleService).create(any(Article.class));

        mockMvc.perform(post("/article")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.magazineName").value(testArticle.getMagazineName()));
    }

    @Test
    void create_article_validationFailed() throws Exception {

        testArticle.setMagazineName("");
        String content = objectMapper.writeValueAsString(testArticle);

        doThrow(new StatusException("Validation failed", HttpStatus.BAD_REQUEST)).when(iArticleService).create(any(Article.class));

        mockMvc.perform(post("/article")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus").value("400"));
    }

    @Test
    void find_article_successfully() throws Exception {

        doReturn(testArticle).when(iArticleService).find(anyLong());

        mockMvc.perform(get("/article/" + 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author.name").value(testArticle.getAuthor().getName()));
    }

    @Test
    void find_article_notFound() throws Exception {

        doThrow(new StatusException("Id not found", HttpStatus.BAD_REQUEST)).when(iArticleService).find(anyLong());

        mockMvc.perform(get("/article/" + 1))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus").value("400"));

    }

    @Test
    void update_article_successfully() throws Exception {

        String content = objectMapper.writeValueAsString(testArticle);

        doAnswer(returnsFirstArg()).when(iArticleService).update(any(Article.class));

        mockMvc.perform(put("/article")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.magazineName").value(testArticle.getMagazineName()));
    }

    @Test
    void update_article_validationFailed() throws Exception {

        testArticle.setMagazineName("");
        String content = objectMapper.writeValueAsString(testArticle);

        doThrow(new StatusException("Validation failed", HttpStatus.BAD_REQUEST)).when(iArticleService).update(any(Article.class));

        mockMvc.perform(put("/article")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus").value("400"));
    }

    @Test
    void delete_article_successfully() throws Exception {

        mockMvc.perform(delete("/article/" + 1))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void delete_article_notFound() throws Exception {

        doThrow(new StatusException("Id not found", HttpStatus.BAD_REQUEST)).when(iArticleService).delete(anyLong());

        mockMvc.perform(delete("/article/" + 1))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    void findAll_articles_sorted() throws Exception {

        List<Article> articleList = IntStream.range(0, 10)
                .mapToObj(value -> new Article(null,
                        null,
                        LocalDate.now().plusDays((long) (Math.random() * 100)),
                        "This is magazine name number: " + value))
                .sorted((o1, o2) -> o2.getPublicationDate().compareTo(o1.getPublicationDate()))
                .collect(Collectors.toList());

        doReturn(articleList).when(iArticleService).findAllSorted(any());

        mockMvc.perform(get("/article/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].publicationDate").value(articleList.get(0).getPublicationDate().toString()));

    }

    @Test
    void findByKeyword_articles_successfully() throws Exception {

        doReturn(List.of(testArticle)).when(iArticleService).findByKeyword("title");

        mockMvc.perform(get("/article/keyword/title"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].articleContent.title").value(testArticle.getArticleContent().getTitle()));

    }
}