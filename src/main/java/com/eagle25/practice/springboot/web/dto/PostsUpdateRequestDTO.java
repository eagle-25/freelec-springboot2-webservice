package com.eagle25.practice.springboot.web.dto;

import com.eagle25.practice.springboot.config.auth.dto.SessionUser;
import com.eagle25.practice.springboot.domain.users.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostsUpdateRequestDTO {
    private String title;
    private String content;
    private SessionUser sessionUser;
    private List<Long> removedAttachmentIds;
    private List<MultipartFile> addedAttachments;

    @Builder
    public PostsUpdateRequestDTO(String title, String content, SessionUser sessionUser, List<Long> removedAttachmentIds, List<MultipartFile> addedAttachments)
    {
        this.title = title;
        this.content = content;
        this.sessionUser = sessionUser;
        this.removedAttachmentIds = removedAttachmentIds;
        this.addedAttachments = addedAttachments;
    }
}
