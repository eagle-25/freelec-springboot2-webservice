package com.eagle25.practice.springboot.domain.users;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    GUEST("ROLE_GUEST", "손님"),
    USER("ROLE_USER", "일반사용자");

    /*
    * Spring Security에서는 권한 코드에 항상 'ROLE_' 접두사로 붙어야 한다.
    * 그래서 권한 코드를 ROLE_GUEST, ROLE_USER로 설정하였다.
    * */

    private final String key;
    private final String title;
}
