package com.eagle25.practice.springboot.web;

import com.eagle25.practice.springboot.config.auth.SecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 스프링 부트와 Junit 사이의 연결자 역할을 함
@RunWith(SpringRunner.class)

// 스프링 테스트 어노테이션 중, Web(Spring MVC)에 집중할 수 있는 어노테이션이다.
@WebMvcTest(controllers = HelloController.class,
excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = SecurityConfig.class)
})
/**
 * @WebMvcTest excludeFilters
 *
 * @WebMvcTests는 WebSecurityConfigurerAdapter, WebMvcConfigurer를 비롯한 @ControllerAdvice, @Controller를 읽는다.
 * 즉, @Repository, @Service, @Component는 스캔 대상이 아니다.
 *
 * SecurityConfig을 제외하지 않을 경우, SecurityConfig 생성을 위한 CustomOAuthUserService는 읽을 수 없어 에러가 발생한다.
 * 따라서, excludeFilters를 사용해 SecurityConfig을 스캔 대상에서 제외하도록 명시적으로 지정하였다.
 */
public class HelloControllerTest {

    // 스프링이 관리하는 Bean을 주입받는다.
    @Autowired
    // 웹 API를 테스트 할 때 사용한다.
    // 스프링 MVC 테스트의 시작점이며, 이 클래스를 통해 HTTP GET, Post 등에 대한 API테스트를 할 수 있다.
    private MockMvc mvc;

    @WithMockUser(roles="USER")
    @Test
    public void hello가_리턴된다() throws Exception {
        String hello = "hello";

        mvc.perform(get("/hello")) // MockMVC를 통해 /hello 주소로  HTTP GET 요청을 한다.
                .andExpect(status().isOk())
                .andExpect(content().string(hello));
    }

    @WithMockUser(roles="USER")
    @Test
    public void helloDTO가_리턴된다() throws Exception {
        String name = "hello";
        int amount = 1000;

        mvc.perform(
                get("/hello/dto")
                        .param("name", name)
                        .param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));

        /*
        jsonPath
        -   JSON 응답값을 필드별로 검증할 수 있는 메소드이다.
        -   $를 기준으로 필드명을 명시한다.
         */
    }
}
