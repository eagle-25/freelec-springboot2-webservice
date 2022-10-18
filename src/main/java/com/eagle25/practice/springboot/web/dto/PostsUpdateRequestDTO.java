package com.eagle25.practice.springboot.web.dto;

import com.eagle25.practice.springboot.config.auth.dto.SessionUser;
import com.eagle25.practice.springboot.domain.users.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsUpdateRequestDTO {
    private String title;
    private String content;
    private SessionUser sessionUser;

    @Builder
    public PostsUpdateRequestDTO(String title, String content, SessionUser sessionUser)
    {
        this.title = title;
        this.content = content;
        this.sessionUser = sessionUser;
    }
}
