package com.jackob101.rekrutacja.service.implementation;

import com.jackob101.rekrutacja.model.Article;
import com.jackob101.rekrutacja.repository.ArticleRepository;
import com.jackob101.rekrutacja.service.definition.IArticleService;
import com.jackob101.rekrutacja.service.definition.IBaseService;
import com.jackob101.rekrutacja.validation.groups.OnCreate;
import com.jackob101.rekrutacja.validation.groups.OnUpdate;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService extends IBaseService<Article> implements IArticleService{

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository, Validator validator) {
        super(validator, "Article");
        this.articleRepository = articleRepository;
    }

    @Override
    public Article find(Long id) {

        checkId(id);

        Optional<Article> optionalArticle = articleRepository.findById(id);

        return optionalArticle.orElseThrow(() -> new RuntimeException("Article with ID: " + id + " was not found"));
    }

    @Override
    public void delete(Long id) {

        checkId(id);

        if (!articleRepository.existsById(id))
            throw new RuntimeException("Article with ID: " + id + " was not found");

        articleRepository.deleteById(id);

        if (articleRepository.existsById(id))
            throw new RuntimeException("Could not delete Article with ID: " + id);

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
            throw new RuntimeException("Article with ID: " + article.getId() + " doesn't exists");

        return articleRepository.save(article);
    }

    @Override
    public Article create(Article article) {
        validate(article, OnCreate.class);

        if(article.getId() != null)
            if (articleRepository.existsById(article.getId()))
                throw new RuntimeException("Could not create Article because ID:" + article.getId() + " is already taken");

        return articleRepository.save(article);
    }


}
