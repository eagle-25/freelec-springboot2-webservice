package com.eagle25.practice.springboot.web;

import com.eagle25.practice.springboot.config.auth.LoginUser;
import com.eagle25.practice.springboot.config.auth.dto.SessionUser;
import com.eagle25.practice.springboot.service.attachments.AttachmentsService;
import com.eagle25.practice.springboot.service.posts.PostsService;
import com.eagle25.practice.springboot.web.dto.PostsResponseDTO;
import com.eagle25.practice.springboot.web.dto.PostsSaveRequestDTO;
import com.eagle25.practice.springboot.web.dto.PostsUpdateRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostAPIController {

    private final PostsService postsService;
    private final AttachmentsService attachmentsService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestParam("title") String title,
                     @RequestParam("content") String content,
                     @RequestParam(value = "attachments", required = false) List<MultipartFile> files,
                     @LoginUser SessionUser user) {

        var data = PostsSaveRequestDTO.builder()
                .title(title)
                .author(user.getEmail())
                .content(content)
                .build();

        var postId = postsService.save(data);

        // s3 section
        if(files == null) return postId;

        try {
            attachmentsService
                    .uploadAll(postId, files);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return postId;
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id,
                       @LoginUser SessionUser user,
                       @RequestParam("title") String title,
                       @RequestParam("content") String content,
                       @RequestParam(value = "removedAttachments", required = false) List<Long> removedAttachmentIds,
                       @RequestParam(value = "addedAttachments", required = false) List<MultipartFile> addedAttachments) {

        var requestDTO = PostsUpdateRequestDTO.builder()
                .title(title)
                .content(content)
                .sessionUser(user)
                .build();

        attachmentsService
                .deleteObjectsById(removedAttachmentIds);



        try {
            if(addedAttachments != null) {
                attachmentsService
                        .uploadAll(id, addedAttachments);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return postsService
                .update(id, requestDTO);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDTO findById(@PathVariable Long id)
    {
        return postsService.findById(id);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id, @LoginUser SessionUser user)
    {
        attachmentsService
                .deleteObjectsByPostId(id);

        postsService
                .delete(id, user);

        return id;
    }
}
