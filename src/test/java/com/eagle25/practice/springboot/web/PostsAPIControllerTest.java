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
@SpringBootTest 어노테이션을 사용한 이유.

1. @WebMVCTest의 경우 JPA 기능이 동작하지 않기 때문임. (Controller와 ControllerAdvice등 외부 연동과 관련된 부분만 활성화됨)

따라서 JPA까지 한번에 테스트 할 때는 @SpringBootTest와 TestRestTemplate 어노테이션을 사용하는게 적합하다.
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

    @Before // 매번 테스트가 시작되기 전에 MockMVC 인스턴스를 생성한다.
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
     * 인증된 모의(가짜) 사용자를 만들어 테스트에 사용한다.
     * roles에 권한을 추가할 수 있다.
     * 이 어노테이션으로 인해 ROLE_USER 권한을 가진 사용자가 API를 요청하는 것과 동일한 효과를 가지게 된다.
     */
    public void Posts_등록된다() throws Exception {
        //given
        String title = "title";
        String content = "content";

        var httpSession = new MockHttpSession();
        httpSession.setAttribute("user", user);

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        mvc.perform((post(url) // 생성된 MockMVC 인스턴스를 생성한다.
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
    public void Posts_수정된다() throws Exception {
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
