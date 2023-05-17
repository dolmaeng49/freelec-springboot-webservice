## 웹게시판 서비스

http://ec2-13-209-127-190.ap-northeast-2.compute.amazonaws.com/

- 주요기능
  - 소셜로그인
  - 게시글 작성 / 수정


- 개발환경
  - Java 8
  - Springboot 2.1.7
  - Gradle 4.8
  - JPA


- 빌드 / 배포 / 운영 환경
  - Travis CI 통한 Build
  - AWS S3 에 빌드한 파일 저장
  - AWS Code Deploy -> EC2 에 배포
  - Nginx 통한 무중단 서비스





---  

>본 프로젝트는 아래 도서, 프로젝트를 참고하였습니다.
>>이동욱, _스프링 부트와 AWS로 혼자 구현하는 웹서비스_ (2019 프리렉).
>>https://github.com/jojoldu/freelec-springboot2-webservice
