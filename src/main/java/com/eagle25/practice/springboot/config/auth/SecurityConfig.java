package com.eagle25.practice.springboot.config.auth;

import com.eagle25.practice.springboot.domain.users.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity // Spring Security 설정을 활성화 시켜준다.
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()// h2 화면을 사용하기 위해 srf, headers.frameOption을 disable 시켜준다.
                .and()
                    .authorizeRequests() // url별 권한 관리를 설정하는 옵션의 시작점
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll() // "/" 등 지정된 URL들은 permitAll() 옵션을 통해 전체 열람 권한을 부여함.
                    .antMatchers("/api/v1/**").hasRole(Role.GUEST.name()) // /api/v1/** 주소를 가진 API는 USER 권한을 가진 사람만 가능하도록 함. | 일시적으로 모든 사용자가 글을 작성할 수 있도록 허용함.
                    .anyRequest() // 설정된 값들 이외의 URL
                        .authenticated() // 나머지 URL은 인증된(로그인한) 사용자만 접근을 허용함.
                .and()
                    .logout() // logout 기능에 대한 여러 설정의 진입점.
                        .logoutSuccessUrl("/") // 로그아웃 성공시 / 주소로 이동함.
                .and()
                    .oauth2Login() // oAuth2 로그인 기능에 대한 여러 설정의 진입점.
                        .userInfoEndpoint() // oAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당함.
                            .userService(customOAuth2UserService); // 로그인 성공시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록. 추가로 진행하고자 하는 기능들을 명시할 수 있음.
    }
}
