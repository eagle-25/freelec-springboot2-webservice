package com.eagle25.practice.springboot.config.auth.dto;

import com.eagle25.practice.springboot.domain.users.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

    // SessionUser에는 인증된 사용자 정보만 필요하다.
    // 그 외의 정보들은 필요없는 관계로, name, email, picture 필드만 생성한다.

    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
