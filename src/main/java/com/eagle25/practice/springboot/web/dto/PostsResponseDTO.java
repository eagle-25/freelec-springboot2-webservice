package com.eagle25.practice.springboot.web.dto;

import com.eagle25.practice.springboot.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostsResponseDTO {
    private Long id;
    private String title;
    private String content;
    private String authorEmail;
    private String authorName;
    private String attachmentId;

    // 굳이 모든 필드를 가진 생성자가 필요하지 않다.
    // 간단하게 Entity를 받아 처리하도록 한다.
    @Builder
    public PostsResponseDTO(Long id, String title, String content, String authorEmail, String authorName, String attachmentId)
    {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorEmail = authorEmail;
        this.authorName = authorName;
        this.attachmentId = attachmentId;
    }
}
