package com.eagle25.practice.springboot.config.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Target(ElementType.PARAMETER)
 * 어노테이션이 생성될 수 있는 위치를 지정함.
 * ElementType.PARAMETER로 지정하였으니, 메소드의 파라메터로 선언된 객체에서만 사용 가능.
 * 이 외에도 클래스 선언문에 쓸 수 있는 Type등이 있음.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
/**
 * @interface
 * 이 파일을 어노테이션 클래스로 지정한다.
 */
public @interface LoginUser {
}
