package com.ll.sideproject.domain.article.controller;

import com.ll.sideproject.domain.article.entity.Article;
import com.ll.sideproject.domain.article.service.ArticleService;
import com.ll.sideproject.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    // 게시글 생성
    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody Article article, Principal principal) {
        Member author = getLoggedInMember(principal); // 로그인한 사용자 정보
        Article createdArticle = articleService.createArticle(article.getTitle(), article.getContent(), author);
        return ResponseEntity.ok(createdArticle);
    }

    // 전체 게시글 조회
    @GetMapping
    public ResponseEntity<List<Article>> getAllArticles() {
        return ResponseEntity.ok(articleService.getAllArticles());
    }

    // 게시글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
        return articleService.getArticleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody Article article) {
        Article updatedArticle = articleService.updateArticle(id, article.getTitle(), article.getContent());
        return ResponseEntity.ok(updatedArticle);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }

    private Member getLoggedInMember(Principal principal) {
        // Principal로부터 로그인한 사용자 정보 조회
        // 실제 구현 시, 사용자 정보를 가져오는 로직 추가 필요
        return Member.builder()
                .username(principal.getName())
                .build();
    }
}
