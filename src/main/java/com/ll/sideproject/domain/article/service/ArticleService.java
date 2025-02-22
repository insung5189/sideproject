package com.ll.sideproject.domain.article.service;

import com.ll.sideproject.domain.article.entity.Article;
import com.ll.sideproject.domain.article.repository.ArticleRepository;
import com.ll.sideproject.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Article createArticle(String title, String content, Member author) {
        Article article = Article.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
        return articleRepository.save(article);
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Optional<Article> getArticleById(Long id) {
        return articleRepository.findById(id);
    }

    public Article updateArticle(Long id, String title, String content) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found"));

        article.setTitle(title);
        article.setContent(content);
        return articleRepository.save(article);
    }

    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }
}
