package com.eagle25.practice.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;


/*
 Entity클래스와 기본 Entity Repository는 함께 위치해야 한다.

 이유:
    1. 둘은 아주 밀접한 관계이다.
    2. Entity 클래스는 기본 Repository 없이는 제대로 역할을 할 수가 없다.
 */

public interface PostsRepository extends JpaRepository<Posts, Long> { // JpaRepository<Entity class, PK Type>
    //JPARepository<Entity class, PK type>을 상속하면 자동으로 CRUD 메소드가 생성된다.
}
