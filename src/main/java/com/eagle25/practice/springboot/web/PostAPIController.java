package com.eagle25.practice.springboot.web;

import com.eagle25.practice.springboot.config.auth.LoginUser;
import com.eagle25.practice.springboot.config.auth.dto.SessionUser;
import com.eagle25.practice.springboot.service.posts.PostsService;
import com.eagle25.practice.springboot.web.dto.PostsResponseDTO;
import com.eagle25.practice.springboot.web.dto.PostsSaveRequestDTO;
import com.eagle25.practice.springboot.web.dto.PostsUpdateRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class PostAPIController {

    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody Map<String, Object> req, @LoginUser SessionUser user) {

        var data = PostsSaveRequestDTO.builder()
                .title(req.get("title").toString())
                .author(user.getEmail())
                .content(req.get("content").toString())
                .build();

        return postsService.save(data);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody Map<String, Object> req, @LoginUser SessionUser user) {

        var requestDTO = PostsUpdateRequestDTO.builder()
                .title(req.get("title").toString())
                .content(req.get("content").toString())
                .sessionUser(user)
                .build();

        return postsService.update(id, requestDTO);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDTO findById(@PathVariable Long id)
    {
        return postsService.findById(id);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id, @LoginUser SessionUser user)
    {
        postsService.delete(id, user);
        return id;
    }
}
