package com.eagle25.practice.springboot.config;

import com.eagle25.practice.springboot.config.auth.LoginUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final LoginUserArgumentResolver loginUserArgumentResolver;

    /**
     * HandlerMethodArgumentResolver는 항상 WebMvcConfigurer의 addArgumentResolvers를 통해 추가해야함.
     *
     * HandlerMethodArgumentResolver는 한가지 기능을 지원한다.
     * : 조건에 맞는 경우, 메소드가 있다면 HandlerMethodArgumentResolver의 구현체가 지정한 값으로 해당 메소드의 파라메터를 넘길 수 있다.
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers)
    {
        // LoginUserArgumentResolver가 Spring에서 인식될 수 있도록 추가.
        argumentResolvers.add(loginUserArgumentResolver);
    }
}
