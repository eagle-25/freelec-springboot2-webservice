package com.eagle25.practice.springboot.config.auth;

import com.eagle25.practice.springboot.config.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final HttpSession httpSession;

    @Override
    public boolean supportsParameter(MethodParameter parameter) // 컨트롤러 메서드의 특정 파라메터를 지원하는지 판단한다.
    {
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;

        boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());

        return isLoginUserAnnotation && isUserClass;
        /**
         * 다음의 경우 true를 반환함.
         * 1. 파라메터에 @LoginUser 어노테이션이 붙어있음.
         * 2. 파라메터 클래스 타입이 SessionUser.class임.
         */
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, // 파라메터에 전달할 객체를 생성함.
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory)
        throws Exception {
        return httpSession.getAttribute("user");
    }
}
