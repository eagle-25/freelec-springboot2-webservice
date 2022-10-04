package com.eagle25.practice.springboot.web.dto;

import com.eagle25.practice.springboot.domain.posts.Posts;
import lombok.Getter;

@Getter
public class PostsResponseDTO {
    private Long id;
    private String title;
    private String content;
    private String author;

    // 굳이 모든 필드를 가진 생성자가 필요하지 않다.
    // 간단하게 Entity를 받아 처리하도록 한다.
    public PostsResponseDTO(Posts entity)
    {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getTitle();
        this.author = entity.getAuthor();
    }
}
