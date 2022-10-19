# freelec-springboot2-webservice

이 레포지토리는 이동욱 저 - '스프링 부트와 AWS로 혼자 구현하는 웹 서비스' 책을 따라하는 프로젝트를 저장하기 위한 레포지토리 입니다.

<img src="https://user-images.githubusercontent.com/110667795/196145258-7f3b7d18-3e00-423c-a99d-944e3d199b16.png" width="150"/>

**[스프링 부트와 AWS로 혼자 구현하는 웹 서비스]**

이 프로젝트에서는 'SpringBoot2' 를 사용해 간단한 게시판을 구현하였습니다. 

인프라 구성을 위해 AWS와 Travis CI를 사용해 CI/CD를 구축하였으며, Nginx를 사용해 무중단 배포까지 구성하는 작업을 수행하였습니다.

프로젝트의 결과물은 아래 [링크](http://peter-home-automation.link/)에서 확인하실 수 있습니다.

배포 주소: http://peter-home-automation.link/ [^1]

[^1]: 기존에 개인적인 용도로 구입한 도메인을 활용하여 배포하였습니다.

# 프로젝트 작업 내용

이 항목에서는 프로젝트에서 구현한 내용과, 사용한 기술을 기록니다.

## 프로젝트 세부 기능

프로젝트에서 구현한 게시판의 기능은 다음과 같습니다.

1. 글 쓰기, 수정, 삭제, 조회 (CRUD)
2. OAuth 2.0을 사용한 로그인 기능 구현 (구글, 네이버)

### 구현 결과물
아래 사진은 이 프로젝트의 결과물입니다.

<img width="717" alt="image" src="https://user-images.githubusercontent.com/110667795/196593082-2039d984-0f93-4c1e-a68f-131180ff4435.png">

## 프로그래밍

### Frameworks & Library
1. SpringBoot2
2. JPA, Hibernate
3. Gradle
4. Test(JUnit4)
5. lombok
6. SpringSecurity, Oauth2 Client

### Tools
1. IntelliJ


## 인프라

이 항목에서는 freelec-springboot2-webservice 프로젝트의 운영을 위해 구성한 인프라에 대해 설명합니다.

설명은 다음 세 가지의 항목으로 구분하여 설명합니다.

1. 운영
2. 배포 자동화
3. nginX를 사용한 무중단 배포


### 1. 운영

<img width="539" alt="image" src="https://user-images.githubusercontent.com/110667795/196603164-5934f66a-f9a1-49df-9b8e-d0431fb5dadb.png">

1. 클라이언트는 Route 53에 등록된 'peter-home-automation.link' 도메인을 통해 서버의 ip를 얻습니다.
2. 얻은 ip를 통해 application이 운영되고 있는 ec2에 http로 접근합니다.
3. app은 rds에서 운영되는 mariaDB에 데이터를 저장합니다.

### 2. 배포 자동화

<img width="437" alt="image" src="https://user-images.githubusercontent.com/110667795/196605461-dbb9aced-8a35-4283-9498-77a23296a916.png">

1. 개발자는 github repository의 origin/master에 코드를 push합니다.
2. 이 동작은 Travis CI의 빌드에 트리거 됩니다. 트리거 된 빌드는 특정 절차를 통해 .jar를 만듭니다.
3. 생성된 .jar은 s3에 저장됩니다.
4. Travis CI의 빌드가 완료되면 CodeDeploy를 트리거 합니다.
5. codeDeploy는 새로운 .jar 파일을 s3로부터 받아온 뒤, .appspec.yml 파일에서 지정된 스크립트를 사용하여 배포합니다.

### 3. nginX를 사용한 무중단 배포

<img width="374" alt="image" src="https://user-images.githubusercontent.com/110667795/196604056-61c8881b-25cb-48b2-9b13-bd30a3587fc7.png">

1. nginX는 80번 tcp port를 listening 합니다.
2. .appspec.yml 파일에서 지정된 switch.sh 스크립트에 의해 매 배포시마다 8081과 8082의 포트가 스위칭됩니다.
3. 포트 스위칭 시 /etc/nginx/conf.d/service-url.inc 파일에 저장되어 있는 service_url의 포트를 변경합니다.


### 인프라 전체 구성도
상기 항목을 모두 종합하여 나타낸 인프라 구성도는 다음과 같습니다.
<img width="643" alt="image" src="https://user-images.githubusercontent.com/110667795/196605483-0bfbfc63-3319-4384-a36e-3a007c90479a.png">
