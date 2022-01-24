package com.jackob101.rekrutacja.repository;

import com.jackob101.rekrutacja.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("SELECT a FROM Article a WHERE a.articleContent.title LIKE CONCAT('%', :keyword, '%') OR a.articleContent.body LIKE CONCAT('%', :keyword, '%')")
    List<Article> findByKeyword(@Param("keyword") String keyword);
}
