package com.eagle25.practice.springboot.domain.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email); // 소셜 로그인으로 반환된 값 중 email을 사용하여 가입 여부를 확인하기 위한 메소드 이다.
}

