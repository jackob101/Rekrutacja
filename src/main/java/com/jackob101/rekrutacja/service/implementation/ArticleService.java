package com.jackob101.rekrutacja.service.implementation;

import com.jackob101.rekrutacja.exception.StatusException;
import com.jackob101.rekrutacja.model.Article;
import com.jackob101.rekrutacja.repository.ArticleRepository;
import com.jackob101.rekrutacja.service.definition.IArticleService;
import com.jackob101.rekrutacja.service.definition.BaseService;
import com.jackob101.rekrutacja.validation.groups.OnCreate;
import com.jackob101.rekrutacja.validation.groups.OnUpdate;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService extends BaseService<Article> implements IArticleService{

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository, Validator validator) {
        super(validator, "Article");
        this.articleRepository = articleRepository;
    }

    @Override
    public Article find(Long id) {

        checkId(id);

        Optional<Article> optionalArticle = articleRepository.findById(id);

        return optionalArticle.orElseThrow(() -> new StatusException("Article with ID: " + id + " was not found", HttpStatus.BAD_REQUEST));
    }

    @Override
    public void delete(Long id) {

        checkId(id);

        if (!articleRepository.existsById(id))
            throw new StatusException("Article with ID: " + id + " was not found", HttpStatus.BAD_REQUEST);

        articleRepository.deleteById(id);

        if (articleRepository.existsById(id))
            throw new StatusException("Could not delete Article with ID: " + id, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public List<Article> findAllSorted(Sort.Direction sortDirection) {
        return articleRepository.findAll(Sort.by(sortDirection, "publicationDate"));
    }

    @Override
    public Article update(Article article) {

        validate(article, OnUpdate.class);

        boolean isFound = articleRepository.existsById(article.getId());
        if(!isFound)
            throw new StatusException("Article with ID: " + article.getId() + " doesn't exists", HttpStatus.BAD_REQUEST);

        return articleRepository.save(article);
    }

    @Override
    public Article create(Article article) {
        validate(article, OnCreate.class);

        if(article.getId() != null)
            if (articleRepository.existsById(article.getId()))
                throw new StatusException("Could not create Article because ID:" + article.getId() + " is already taken", HttpStatus.BAD_REQUEST);

        return articleRepository.save(article);
    }

    @Override
    public List<Article> findByKeyword(String keyword) {

        if(keyword == null || keyword.isBlank())
            throw new StatusException("Keyword cannot be null or blank",HttpStatus.BAD_REQUEST);

        return articleRepository.findByKeyword(keyword);
    }


}
