package com.eagle25.practice.springboot.web.dto;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class HelloResponseDTOTest {

    @Test
    public void 롬북_기능_테스트() {
        // given
        String name = "test";
        int amount = 100;

        // when
        HelloResponseDTO dto = new HelloResponseDTO(name, amount);

        // Then

        /*
        [assertThat]
        * asertj라는 테스트 검증 라이브러리의 검증 메소드 이다.
        * 검증하고 싶은 대상을 인자로 받는다.
        * 메소드 체이닝을 지원한다. */
        assertThat(dto.getName())
                .isEqualTo(name);

        assertThat(dto.getAmount())
                .isEqualTo(amount);

        /*
        Junit이 아닌, assertj를 사용한 이유.
        1.  CoreMatcher와 같은 추가적인 라이브러리를 요구하지 않는다.
            - Junit의 assertThat을 쓰게 되면 is()와 같이 coreMatchers 라이브러리가 필요하다.
        2.  자동완성이 확실하게 지원된다.
            - IDE에서는 CoreMatchers와 같은 라이브러리의 자동 완성 지원이 약하다.
        */
    }
}
