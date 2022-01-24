package com.jackob101.rekrutacja.api;

import com.jackob101.rekrutacja.model.Article;
import com.jackob101.rekrutacja.service.definition.IArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "article", produces = MediaType.APPLICATION_JSON_VALUE)
public class ArticleApi {

    private final IArticleService articleService;

    public ArticleApi(IArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("all")
    public ResponseEntity<Object> getAllSorted() {

        log.info("Fetching all articles");

        List<Article> allSorted = articleService.findAllSorted(Sort.Direction.DESC);

        log.info("Articles fetched successfully");

        return ResponseEntity.ok(allSorted);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long id) {

        log.info("Fetching article with ID: " + id);

        Article byId = articleService.find(id);

        log.info("Article with ID: " + id + " fetched successfully");

        return ResponseEntity.ok(byId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createArticle(@RequestBody Article article) throws URISyntaxException {

        log.info("Creating new article");

        Article saved = articleService.create(article);

        log.info("New article with ID: " + saved.getId() + " created successfully");

        return ResponseEntity.created(new URI("/article/" + saved.getId()))
                .body(saved);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateArticle(@RequestBody Article article) {

        log.info("Updating article with ID: " + article.getId());

        Article updated = articleService.update(article);

        log.info("Article with ID: " + updated.getId() + " updated successfully");

        return ResponseEntity.ok(updated);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteArticle(@PathVariable Long id) {

        log.info("Deleting Article with ID: " + id);

        articleService.delete(id);

        log.info("Article with ID: " + id + " deleted successfully");

        return ResponseEntity.ok("Entity deleted successfully");
    }
}
