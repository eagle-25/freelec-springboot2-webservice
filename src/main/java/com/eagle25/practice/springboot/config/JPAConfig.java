package com.eagle25.practice.springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JPAConfig {
}
/**
 * Test코드 수행 시 @WebMvcTest를 사용한 테스트는 다음 에러가 발생됨.
 * : java.lang.IllegalArgumentException: At least one JPA metamodel must be present!
 *
 * 이는 @EnableJpaAuditing으로 인해 발생된 예외임.
 * @EnableJpaAuditing을 사용하기 위해서는 최소 한 개 이상의 @Entity 클래스가 필요함.
 * @WebMvcTest는 기본적으로 @Entity 클래스가 없음.
 * 따라서, 상기 2개의 내용이 상충되어 예외가 발생됨.
 *
 * 기존 코드의 경우 Application 클래스에 @EnableJpaAuditing는 @SpringBootApplication에 같이 있었음.
 * 이 경우, @WebMvcTest에서도 @EnableJpaAuditing을 스캔하게 됨.
 * @EnableJpaAuditing과 @SpringBootApplication를 분리하면 상기 예외를 해결할 수 있음.
 *
 * @WebMvcTest는 @Configuration이 지정된 클래스를 스캔하지 않음.
 * 이 파일과 같이 별도의 클래스를 만들고, @EnableJpaAuditing과 @Configuration을 같이 지정하여 상기 문제를 해결할 수 있음.
 */