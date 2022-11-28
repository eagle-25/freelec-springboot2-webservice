package com.eagle25.practice.springboot.web.dto;

import com.eagle25.practice.springboot.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDTO {
    private String title;
    private String content;
    private String author;

    @Builder
    public PostsSaveRequestDTO(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }
    /*
    DTO를 사용하는 이유.

        - 수많은 서비스나 로직이 Entity 클래스를 기준으로 동작함. Entity에 변경이 발생되면 수많은 클래스에 영향을 끼치게 됨.
        - Request / Response용 DTO는 View만을 위한 객체로, 자주 변경될 가능성이 높음. 그러나 Entity에 비해 다른 클래스에는 영향을 덜 끼침.
        - Controller에서 결괏값으로 여러 테이블을 조인해서 줘야 할 경우가 빈번하며, 이는 Entity 클래스만으로 표현하기 한계가 있음.

    따라서, DTO를 사용하여 View Layer와 DB Layer를 철저하게 분리해줘야 할 필요가 있음.

     */
}
