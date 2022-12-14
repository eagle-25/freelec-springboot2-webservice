package com.eagle25.practice.springboot.web;

import com.eagle25.practice.springboot.config.auth.LoginUser;
import com.eagle25.practice.springboot.config.auth.dto.SessionUser;
import com.eagle25.practice.springboot.domain.posts.Posts;
import com.eagle25.practice.springboot.domain.posts.PostsRepository;
import com.eagle25.practice.springboot.domain.users.Role;
import com.eagle25.practice.springboot.domain.users.User;
import com.eagle25.practice.springboot.domain.users.UserRepository;
import com.eagle25.practice.springboot.web.dto.PostsSaveRequestDTO;
import com.eagle25.practice.springboot.web.dto.PostsUpdateRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.var;
import net.minidev.json.JSONArray;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Session;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
/*
@SpringBootTest ?????????????????? ????????? ??????.

1. @WebMVCTest??? ?????? JPA ????????? ???????????? ?????? ?????????. (Controller??? ControllerAdvice??? ?????? ????????? ????????? ????????? ????????????)

????????? JPA?????? ????????? ????????? ??? ?????? @SpringBootTest??? TestRestTemplate ?????????????????? ??????????????? ????????????.
 */
public class PostsAPIControllerTest {
    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }
    @LocalServerPort
    private int port;
    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private SessionUser user;

    @Before // ?????? ???????????? ???????????? ?????? MockMVC ??????????????? ????????????.
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        user = new SessionUser(User.builder()
                .name("testUser")
                .email("tmdwns02556@gmail.com")
                .picture("")
                .role(Role.USER)
                .build());
    }

    @After
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles="GUEST")
    /**
     * @WithMockUser(roles="USER")
     * ????????? ??????(??????) ???????????? ????????? ???????????? ????????????.
     * roles??? ????????? ????????? ??? ??????.
     * ??? ????????????????????? ?????? ROLE_USER ????????? ?????? ???????????? API??? ???????????? ?????? ????????? ????????? ????????? ??????.
     */
    public void Posts_????????????() throws Exception {
        //given
        String title = "title";
        String content = "content";

        var httpSession = new MockHttpSession();
        httpSession.setAttribute("user", user);

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        mvc.perform((post(url) // ????????? MockMVC ??????????????? ????????????.
                .session(httpSession)
                .param("title", title)
                .param("content", content)
                .param("attachments", "")
                .contentType(MediaType.TEXT_PLAIN)))
                    .andExpect(status().isOk());

        //then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle())
                .isEqualTo(title);
        assertThat(all.get(0).getContent())
                .isEqualTo(content);
        assertThat(all.get(0).getAuthor())
                .isEqualTo(user.getEmail());
    }

    @Test
    @WithMockUser(roles="GUEST")
    public void Posts_????????????() throws Exception {
        //given
        var authorEmail = user.getEmail();

        var savedPostId = postsRepository.save(Posts.builder()
                .title("Title")
                .content("Content")
                .author(authorEmail)
                .build())
                .getId();

        var httpSession = new MockHttpSession();
        httpSession.setAttribute("user", user);

        var expectedTitle = "title2";
        var expectedContent = "content2";
        var url = "http://localhost:" + port + "/api/v1/posts/" + savedPostId;

        //when
        mvc.perform(put(url)
                .session(httpSession)
                .contentType(MediaType.TEXT_PLAIN)
                .param("title", expectedTitle)
                .param("content", expectedContent)
                .param("removedAttachments", "")
                .param("addedAttachments", ""))
                        .andExpect(status().isOk());

        //then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle())
                .isEqualTo(expectedTitle);

        assertThat(all.get(0).getContent())
                .isEqualTo(expectedContent);
    }

}
