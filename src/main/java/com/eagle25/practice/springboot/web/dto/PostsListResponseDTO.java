package com.eagle25.practice.springboot.web.dto;

import com.eagle25.practice.springboot.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostsListResponseDTO {

    private Long id;
    private String title;
    private String authorInfo;
    private LocalDateTime modifiedDate;

    @Builder
    public PostsListResponseDTO(Long id, String title, String authorInfo, LocalDateTime modifiedDate) {
        this.id = id;
        this.title = title;
        this.authorInfo = authorInfo;
        this.modifiedDate = modifiedDate;
    }
}
