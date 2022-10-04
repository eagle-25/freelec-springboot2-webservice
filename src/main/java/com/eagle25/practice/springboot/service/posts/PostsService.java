package com.eagle25.practice.springboot.service.posts;


import com.eagle25.practice.springboot.domain.posts.Posts;
import com.eagle25.practice.springboot.domain.posts.PostsRepository;
import com.eagle25.practice.springboot.web.dto.PostsResponseDTO;
import com.eagle25.practice.springboot.web.dto.PostsSaveRequestDTO;
import com.eagle25.practice.springboot.web.dto.PostsUpdateRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
/*
@RequiredArgsConstructor를 사용하는 이유

생성자를 주입받는 방법은 3가지가 있다.
1. @Autowired
2. setter
3. 생성자

이 중, 가장 권장하는 방식은 생성자로 의존성을 주입하는 방식이다. (@Autowired는 권장하지 않음)

생성자로 의존성을 주입받는 방식은, 의존성 주입이 필요한 모든 필드를 생성자의 매개변수로 추가하여야 한다.
필드에 변경이 있을 경우, 매번 생성자를 수정해야 하는 번거로움이 있다.

lombok의 library인 @RequiredArgsConstructor를 사용하면 클래스 내에 선언된 모든 필드들을 매개변수로 취하는 생성자를 자동으로 만들어 준다.
필드 변경에 따른 생성자를 직접 수정할 필요가 없어 필드 변경이 편해진다.
 */
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDTO requestDTO) {
        return postsRepository.save(requestDTO.toEntity())
                .getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDTO requestDTO)
    {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        posts.update(requestDTO.getTitle(), requestDTO.getContent());

        return id;
    }
    /* Update 기능에 쿼리를 날리는 부분이 없는 이유

    JPA 영속성 컨텍스트 때문이다.
    : 엔티티를 영구적으로 저장하는 환경이다. 따라서, JPA의 핵심 내용은 엔티티가 영속성 컨텍스트에 포함되어 있냐 아니냐로 갈린다.

    JPA의 엔티티 매니저가 활성화된 상태로 트랜잭션 안에서 데이터베이스에서 데이터를 가져오면 영속성 컨텍스트가 유지되었다고 할 수 있다.
    이 상태에서 해당 엔티티의 값을 변경하면 트랜잭션이 끝나는 시점에 엔티티와 매칭된 테이블에 변경분을 반영한다.
    즉, Entity 객체의 값만 변경하면 별도로 update 쿼리를 날릴 필요가 없다.

    이 개념을 Dirty Checking이라고 한다.

     */

    public PostsResponseDTO findById (Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new PostsResponseDTO(entity);
    }
}
