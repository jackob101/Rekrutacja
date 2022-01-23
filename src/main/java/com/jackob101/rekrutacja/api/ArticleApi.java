package com.jackob101.rekrutacja.api;

import com.jackob101.rekrutacja.model.Article;
import com.jackob101.rekrutacja.service.definition.IArticleService;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(value = "article", produces = MediaType.APPLICATION_JSON_VALUE)
public class ArticleApi {

    private final IArticleService articleService;

    public ArticleApi(IArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("all")
    public ResponseEntity<Object> getAllSorted() {

        List<Article> allSorted = articleService.findAllSorted(Sort.Direction.DESC);

        return ResponseEntity.ok(allSorted);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long id) {

        Article byId = articleService.find(id);

        return ResponseEntity.ok(byId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createArticle(@RequestBody Article article) throws URISyntaxException {

        Article saved = articleService.create(article);

        return ResponseEntity.created(new URI("/article/" + saved.getId()))
                .body(saved);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateArticle(@RequestBody Article article){

        Article updated = articleService.update(article);

        return ResponseEntity.ok(updated);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteArticle(@PathVariable Long id){

        articleService.delete(id);

        return ResponseEntity.ok("Entity deleted successfully");
    }
}
