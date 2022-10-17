# freelec-springboot2-webservice

이 레포지토리는 이동욱 저 - '스프링 부트와 AWS로 혼자 구현하는 웹 서비스' 책을 따라하는 프로젝트를 저장하기 위한 레포지토리 입니다.

**[스프링 부트와 AWS로 혼자 구현하는 웹 서비스 예제]**

이 프로젝트에서는 'SpringBoot2' 를 사용해 간단한 게시판을 구현하였습니다. 
인프라 구성을 위해 AWS와 Travis CI를 사용해 CI/CD를 구축하였으며, Nginx를 사용해 무중단 배포까지 구성하는 작업을 수행하였습니다.

# 프로젝트 작업 내용

이 항목에서는 프로젝트에서 구현한 내용과, 사용한 기술등을 나타냅니다.

## 프로젝트 세부 기능

프로젝트에서 구현한 게시판의 기능은 다음과 같습니다.

1. 글 쓰기, 수정, 삭제, 조회 (CRUD)
2. OAuth 2.0을 사용한 로그인 기능 구현 (구글, 네이버)

## 프로그래밍

### Frameworks & Library
1. SpringBoot2
2. JPA, Hibernate
3. Gradle
4. Test(JUnit4)
5. lombok
6. SpringSecurity, Oauth2 클라이언트

### Tools
1. IntelliJ


## 인프라

인프라 구축을 위해 사용한 AWS 서비스는 다음과 같습니다.

1. EC2 (t2.micro)
2. RDS (MariaDB)
4. Travis CI
3. CodeDeploy

## 운영 환경 구성

무중단 배포를 구현하기 위해 다음과 같은 서비스를 사용하였습니다.

1. nginX
