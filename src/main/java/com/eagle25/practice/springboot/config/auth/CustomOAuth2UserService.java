package com.eagle25.practice.springboot.config.auth;

import com.eagle25.practice.springboot.config.auth.dto.OAuthAttributes;
import com.eagle25.practice.springboot.config.auth.dto.SessionUser;
import com.eagle25.practice.springboot.domain.users.User;
import com.eagle25.practice.springboot.domain.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    // 이 클레스에서는 구글 로그인 이후 가져온 사용자의 정보(email, name, picture 등)들을 기반으로 가입 및 정보수정, 세션 저장등의 기능을 지원함.

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        /**
        * 현재 로그인을 진행중인 서비스를 구분하는 코드.
        * ex: naver & google 로그인을 연동했을 경우, 구글 로그인인지 네이버 로그인인지 구분할 용도임.
        */
        String registrationId = userRequest
                .getClientRegistration().getRegistrationId();

        /**
        oAuth2 로그인 진행 시 키가 되는 필드값. (primary key와 같은 의미)
          - 구글의 경우 기본적으로 코드를 지원함. 구글의 기본 코드는 sub임.
          - 네이버, 카카오등은 지원하지 않음.
         */
        String userNameAttributeName = userRequest
                .getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        /**
        OAuth2UserService를 통해 가져온 OAuth2 유저의 attributes를 담을 클래스이다.
          - 이후 네이버 등 다른 소셜 로그인도 이 클래스를 사용한다.
        */
        OAuthAttributes attributes = OAuthAttributes
                .of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);

        /**
        SessionUser

        세션에 사용자 정보를 저장하기 위한 DTO 클래스임.

        User 객체를 사용하지 않은 이유.

          1. User 객채를 그대로 사용하면 에러가 발생됨.
            - 에러 내용: Failed to convert from type [java.lang.Object] to type [byte[]] for value 'com.eagle25.practice.springboot.domain.user.User@~~~~
              이는 User 클래스를 세션에 저장하려고 직렬화를 할 때, User 클래스가 직렬화를 하지 않아 발생된 에러임.

             -> 그렇다면 User 객체에 직렬화 코드를 추가하면 되지 않는가?

          2. User 객체에 직렬화를 추가할 경우 발생될 수 있는 문제
            - User 객체는 엔티티이다. 엔티티는 언제라도 다른 엔티티와 관계를 형성할 수 있다.
            - 예를 들어 1:n, n:n의 관계인 자식 엔티티를 가지고 있다고 가정할 경우, 직렬화 대상에 자식 엔티티까지 포함된다.
              이는 성능 이슈, 부수 효과가 발생하는 문제로 이어질 확률이 높다.

          3. 직렬화 기능을 가진 DTO를 하나 추가로 만들자.
            - 위에서 기술한 이유로, 세션에 필요한 정보만을 포함하고 있는 DTO를 별도로 만들고, 이를 세션에 저장하는 방법이 권장된다.
            - 간단한 데이터 구조 덕분에 운영 및 유지보수 측면에서 얻을 수 있는 이점이 크다.

         Reference: P.188
         */

        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                    attributes.getAttributes(),
                    attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail((attributes.getEmail()))
                .map(entity -> entity.update(attributes.getName(),
                        attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }


}
