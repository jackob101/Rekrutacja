package com.jackob101.rekrutacja.service.definition;

import com.jackob101.rekrutacja.model.Article;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface IArticleService {

    Article find(Long id);
    void delete(Long id);
    List<Article> findAllSorted(Sort.Direction sortDirection);
    Article update(Article article);
    Article create(Article article);
    List<Article> findByKeyword(String keyword);

}
