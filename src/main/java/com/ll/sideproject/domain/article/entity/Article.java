package com.ll.sideproject.domain.article.entity;

import com.ll.sideproject.base.entity.BaseEntity;
import com.ll.sideproject.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "article")
public class Article extends BaseEntity {

    @Column(nullable = false, length = 255)
    private String title; // 게시글 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 게시글 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member author; // 작성자

    @Builder.Default
    @Column(nullable = false)
    private int viewCount = 0; // 조회수

    public void incrementViewCount() {
        this.viewCount++;
    }
}
