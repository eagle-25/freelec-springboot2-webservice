package com.eagle25.practice.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// SpringBootApplication annotation으로 덕분에, 스프링 부트의 자동 설정, 스프링 Bean 읽기와 생성은 모두 자동으로 설정됨
// Spring Framework는 이 aanotation이 있는 위치에서부터 코드를 읽음. 따라서, 이 어노테이션은 항상 프로젝트의 최상단에 위치 하여야 함.

@SpringBootApplication
public class Application {
    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }
    public static void main(String[] args) {
        // 내장 WAS를 실행. (Web Application Server)
        // 스프링은 내장 WAS 사용을 권장하고 있다.
        // 언제 어디서나 같은 환경에서 스프링 부트를 배포할 수 있기 때문이다.
        SpringApplication.run(Application.class, args);
    }
}
