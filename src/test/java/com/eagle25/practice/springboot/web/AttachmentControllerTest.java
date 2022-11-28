package com.eagle25.practice.springboot.web;

import com.eagle25.practice.springboot.config.auth.dto.SessionUser;
import com.eagle25.practice.springboot.domain.attachment.AttachmentRepository;
import com.eagle25.practice.springboot.domain.posts.Posts;
import com.eagle25.practice.springboot.domain.posts.PostsRepository;
import com.eagle25.practice.springboot.domain.users.Role;
import com.eagle25.practice.springboot.domain.users.User;
import com.eagle25.practice.springboot.service.attachments.AttachmentsService;
import lombok.var;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-aws.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AttachmentControllerTest {
    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

    @LocalServerPort
    private int port;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private AttachmentsService attachmentsService;

    private Posts preBuiltPost;
    private SessionUser sessionUser;
    private MockMultipartFile mockFile;
    private MockMvc mvc;

    @Before // 매번 테스트가 시작되기 전에 MockMVC 인스턴스를 생성한다.
    public void setup() {
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        this.sessionUser = new SessionUser(User.builder()
                .name("testUser")
                .email("tmdwns02556@gmail.com")
                .picture("")
                .role(Role.USER)
                .build());

        this.preBuiltPost = postsRepository.save(Posts.builder()
                .title("TestPost")
                .content("content123")
                .author("tmdwns02556@gmail.com")
                .build());

        this.mockFile = new MockMultipartFile(
                "attachment",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
    }

    @After
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
        attachmentsService
            .deleteObjectsByPostId(preBuiltPost.getId());
    }

    @WithMockUser(roles = "GUEST")
    @Transactional
    @Test
    public void test_putObject() throws Exception {
        //given
        var httpSession = new MockHttpSession();
        httpSession.setAttribute("user", sessionUser);

        var url = "http://localhost:" + port + "/api/v1/attachment";

        //when
        var response = mvc.perform((multipart(url) // 생성된 MockMVC 인스턴스를 생성한다.
                        .file(mockFile))
                        .param("ownerPostId", String.valueOf(preBuiltPost.getId()))
                        .session(httpSession)
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

      var assignedAttachmentId = Long.valueOf(response);

      var uploadedFile = attachmentsService
              .getObject(assignedAttachmentId);

      //then
      assertThat(uploadedFile.getBody()).isEqualTo(mockFile.getBytes());
    }

    @WithMockUser(roles="GUEST")
    @Transactional
    @Test
    public void test_deleteObject() throws Exception {
        //given
        var assignedAttachmentId = attachmentsService
                .upload(preBuiltPost.getId(), mockFile);

        var createdUniqueFileName = attachmentRepository
                .getOne(assignedAttachmentId)
                        .getUniqueFileName();

        // verify that if file is successfully uploaded to S3 or not.
        assertThat(attachmentsService
                .getObject(assignedAttachmentId)
                .getBody())
                .isEqualTo(mockFile.getBytes());

        var httpSession = new MockHttpSession();
        httpSession.setAttribute("user", sessionUser);

        var url = "http://localhost:" + port + "/api/v1/attachment/" + assignedAttachmentId;

        //when
        mvc.perform((delete(url))
                .contentType(MediaType.TEXT_PLAIN)
                .session(httpSession))
                .andExpect(status().isOk());

        //then
        assertThat(attachmentRepository
                .existsById(assignedAttachmentId))
                .isFalse();

        assertThat(attachmentsService
                .isObjectExist(createdUniqueFileName));
    }
}