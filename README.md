<br>
<div align="center">
  <img src="https://github.com/Dev-Race/DevRace-backend/assets/56509933/e8dfa33b-8561-4181-9496-e3ec70f41aa8" width="193" height="193" />
  <h3 align="center">Dev Race 🧑‍💻</h3>
  <p align="center">
    실시간 코딩 경쟁 및 채팅 서비스<br>
    <a href="https://github.com/Dev-Race"><strong>Explore the team »</strong></a>
  </p>
</div>
<br>

<details open>
  <summary><strong>&nbsp;📖&nbsp;목차</strong></summary>

1. &nbsp;&nbsp;[🔍 Introduction](#-introduction)
2. &nbsp;&nbsp;[📄 Documents](#-documents)
3. &nbsp;&nbsp;[💻 Architecture](#-architecture)
4. &nbsp;&nbsp;[💡 Tech Stack](#-tech-stack)
5. &nbsp;&nbsp;[🗂️ Database](#%EF%B8%8F-database)
6. &nbsp;&nbsp;[🤝 Git Convention](#-git-convention)
7. &nbsp;&nbsp;[📂 Package Convention](#-package-convention)
8. &nbsp;&nbsp;[👨‍👩‍👧‍👧 Team](#-team)
</details>
<br>



## 🔍 Introduction

### Description
최근 개발자들 사이에서 알고리즘 스터디 모임이 활발히 이루어지고 있습니다.<br>
이를 위해, 함께 알고리즘 문제를 풀고 토의하며 경쟁할 수 있는 실시간 코딩 플랫폼을 제공합니다.<br>
이 플랫폼으로 스터디원들의 실력을 빠르게 향상시키고, 모임의 운영 효율성을 극대화하고자 합니다.

### Main Feature
- 소셜 로그인&nbsp;:&nbsp;&nbsp;Google, Github을 통한 간편한 로그인 제공
- 알고리즘 문제 풀이&nbsp;:&nbsp;&nbsp;'백준 온라인 저지' 문제를 바탕으로 다양한 언어 및 에디터 지원
- 입장 대기열&nbsp;:&nbsp;&nbsp;초대 링크로 스터디원들을 초대하고 동시에 문제 풀이 시작
- 실시간 랭킹&nbsp;:&nbsp;&nbsp;문제 풀이 중 실시간 순위 확인을 통한 경쟁 유도
- 실시간 채팅&nbsp;:&nbsp;&nbsp;문제 풀이 중 힌트 주고받기 및 의견 교환 가능
- 기록 보관&nbsp;:&nbsp;&nbsp;소스코드 및 채팅 내역 저장을 통한 복습 및 재풀이 지원
<br>



## 📄 Documents
- <strong>기간</strong>&nbsp;:&nbsp;&nbsp;2024.04.29 ~ 06.16

- #### [PM] 기획 명세서
  - <details><summary>&nbsp;<a href="https://sahyunjin.notion.site/7384e0322e8d480c8f639ae1a84915fa?v=eda603b2a8844e6fa244492f502ba129&pvs=4">기능 상세 명세서</a></summary><br><img src="https://github.com/tkguswls1106/DevRace-Readme/assets/56509933/38c36139-1d4a-4075-bdf7-6ca33f222466" /></details>
  - <details><summary>&nbsp;<a href="https://docs.google.com/spreadsheets/d/18bLgLlZGMPsulnnqDtMa3JtHX4kLwSqaXHfTSW5EAvw/edit?usp=sharing">WBS 명세서</a></summary><br><img src="https://github.com/tkguswls1106/DevRace-Readme/assets/56509933/b82c38a7-6e32-4038-b92f-7ded9b2ca113" /></details>

- #### [BE] API 명세서
  - <details><summary>&nbsp;<a href="https://sahyunjin.notion.site/2071d1695b254b78a1367ef555d6b820?v=336213c0a3b345f28fdb4ef181044d31&pvs=4">Rest API 명세서</a></summary><br><img src="https://github.com/tkguswls1106/DevRace-Readme/assets/56509933/863cd935-c292-4318-97e2-03b357528a27" /></details>
  - <details><summary>&nbsp;<a href="https://sahyunjin.notion.site/2e577f50c85648cdaae86aeeae66be5a?v=a717219fcb494da39e765bea246b6cfd&pvs=4">WebSocket API 명세서</a></summary><br><img src="https://github.com/tkguswls1106/DevRace-Readme/assets/56509933/a5e52b5a-c3af-405c-89a7-43c24e5f1182" /></details>
  - <details><summary>&nbsp;Swagger API 명세서</summary><br><img src="https://github.com/tkguswls1106/DevRace-Readme/assets/56509933/222b7599-69ba-41aa-9ef5-613fd7837c54" /></details>

- #### To Do List
  - - [x] DTO inner class 리팩토링
  - - [x] URI Security 접근제한 적용
  - - [ ] Demo 사진 및 영상 첨부
<br>



## 💻 Architecture

### System
![devrace_architecture drawio](https://github.com/Dev-Race/DevRace-backend/assets/56509933/103fd5a2-1ba8-4365-81ea-68b11b8218d8)

### Network
![devrace_network_architecture drawio](https://github.com/Dev-Race/DevRace-backend/assets/56509933/a43dae33-515c-4cd0-b8fe-39bfe56d7ab7)

### Detail
<details>
  <summary>&nbsp;<strong>CI/CD flow</strong>&nbsp;:&nbsp;Open!</summary>

#### [ Github ]<br>
- &nbsp;trigger CI/CD
```
1. PR close & Merge into develop branch
```

#### [ Github Actions ]<br>
- &nbsp;<a href="https://github.com/Dev-Race/DevRace-backend/blob/develop/.github/workflows/deploy.yml"><i>.github/workflows/deploy.yml</i></a>&nbsp;<i>+</i>&nbsp;<a href="https://github.com/Dev-Race/DevRace-backend/blob/develop/Dockerfile"><i>Dockerfile</i></a>&nbsp;:&nbsp;&nbsp;start CI/CD
```
2. build gradle
3. Dockerfile : build docker image
4. push image to Docker Hub
5. deploy to AWS Elastic BeanStalk
6. notify CI/CD results to Slack
```

#### [ AWS EB - in deploy ]<br>
- &nbsp;<a href="https://github.com/Dev-Race/DevRace-backend/tree/develop/.ebextensions"><i>.ebextensions</i></a>&nbsp;:&nbsp;&nbsp;set EB Environment
```
7. set timezone & swap memory
8. set RabbitMQ
  - create docker network (if not exists)
  - docker run RabbitMQ image (if not run)
  - connect to docker network (if not connected)
  - enable RabbitMQ plugins (if not enabled)
```
- &nbsp;<a href="https://github.com/Dev-Race/DevRace-backend/blob/develop/Dockerrun.aws.json"><i>Dockerrun.aws.json</i></a>&nbsp;:&nbsp;&nbsp;deploy Spring Container
```
9. pull image from Docker Hub
10. docker run Spring image
```

#### [ AWS EB - after deploy ]<br>
- &nbsp;<a href="https://github.com/Dev-Race/DevRace-backend/blob/develop/.platform/hooks/postdeploy/connect_spring_to_network.sh"><i>.platform/hooks/postdeploy/connect_spring_to_network.sh</i></a>&nbsp;:&nbsp;&nbsp;connect Spring Container
```
11. connect Spring to docker network (if not connected)
12. logging Connect results & monitoring on AWS CloudWatch
==> deploy complete.
```
<br>
</details>

<details>
  <summary>&nbsp;<strong>Notification</strong>&nbsp;:&nbsp;Open!</summary>
  <br>

![slack collaboration_alarm](https://github.com/Dev-Race/DevRace-backend/assets/56509933/888fd684-76bf-4c77-a25b-d4a2ca3daf16)

&#8594;&nbsp;&nbsp;<strong>Slack Notifications</strong> sent by the <strong>GitHub Actions</strong>&nbsp;:<br>
<a href="https://github.com/Dev-Race/DevRace-backend/blob/develop/.github/workflows/slack-pr-open.yml">`💡 PR Open`</a>&nbsp;&nbsp;<a href="https://github.com/Dev-Race/DevRace-backend/blob/develop/.github/workflows/slack-pr-review.yml">`💬 PR Review`<a>&nbsp;&nbsp;<a href="https://github.com/Dev-Race/DevRace-backend/blob/develop/.github/workflows/deploy.yml#L114">`❌ CI/CD Fail`</a>&nbsp;&nbsp;<a href="https://github.com/Dev-Race/DevRace-backend/blob/develop/.github/workflows/deploy.yml#L80">`✅ CI/CD Success`</a>
</details>
<br>



## 💡 Tech Stack
Backend|Security|Database|Deployment|Other|
|:------:|:------:|:------:|:------:|:------:|
|<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=Spring Boot&logoColor=white"/><br><img src="https://img.shields.io/badge/Java-007396?style=flat-square&logo=Java&logoColor=white"/><br><img src="https://img.shields.io/badge/STOMP-164735?style=flat-square&logo=the spriters resource&logoColor=white"/><br><img src="https://img.shields.io/badge/RabbitMQ-FF6600?style=flat-square&logo=RabbitMQ&logoColor=white"/>|<img src="https://img.shields.io/badge/Spring Security-00A98F?style=flat-square&logo=Spring Security&logoColor=white"/><br><img src="https://img.shields.io/badge/JSON Web Token-9933CC?style=flat-square&logo=JSON Web Tokens&logoColor=white"/><br><img src="https://img.shields.io/badge/OAuth2-3423A6?style=flat-square&logo=Authelia&logoColor=white"/>|<img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white"/><br><img src="https://img.shields.io/badge/MongoDB-47A248?style=flat-square&logo=MongoDB&logoColor=white"/>|<img src="https://img.shields.io/badge/Amazon AWS-232F3E?style=flat-square&logo=Amazon Web Services&logoColor=white"/><br><img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=Docker&logoColor=white"/><br><img src="https://img.shields.io/badge/Github Actions-0063DC?style=flat-square&logo=Github Actions&logoColor=white"/>|<img src="https://img.shields.io/badge/Notion-000000?style=flat-square&logo=Notion&logoColor=white"/><br><img src="https://img.shields.io/badge/Swagger-85EA2E?style=flat-square&logo=Swagger&logoColor=black"/><br><img src="https://img.shields.io/badge/Slack-4A154B?style=flat-square&logo=Slack&logoColor=white"/>
```
- Frontend : React, JavaScript, Redux
- Backend : Spring Boot, Java | Security, JWT, OAuth2 | STOMP, RabbitMQ
- Database : MySQL, MongoDB
- Deployment : AWS Amplify, AWS Elastic Beanstalk, Docker, Github Actions
- Documentation : Notion, Swagger
- Notification : Slack
```
&#8594;&nbsp;&nbsp;***BE Version***&nbsp;:&nbsp;&nbsp;Java 17 · Spring Boot 3.1.11
<br><br>



## 🗂️ Database
![devrace DB ERD](https://github.com/Dev-Race/DevRace-backend/assets/56509933/cc34957c-5429-4fd0-b95d-8c9d9024f77e)
<br><br>



## 🤝 Git Convention

### Branch
- Git-Flow 전략
- 반드시 "develop"에서 뻗어나와 develop으로 "merge" 되어야한다.
- `main` : 출시 배포 CI/CD용 branch (미사용)
- `develop` : 개발 개포 CI/CD용 branch
- `feature` : 기능 구현용 branch
- `Issue_종류/#Issue_번호` : branch 생성

### Issue
`✨ Feat`  `🐛 Fix`  `♻️ Refactor`  `✅ Test`<br>
`📁 File`  `📝 Docs`  `🔧 Chore`  `⚙️ Setting`

| 종류             | 내용                                             |
|----------------| ------------------------------------------------ |
| ✨ Feat         | 기능 구현                                          |
| 🐛 Fix         | 버그 수정                                           |
| ♻️ Refactor    | 코드 리팩토링                                         |
| ✅ Test         | 테스트 업무                                        |
| 📁 File        | 파일 이동 또는 삭제, 파일명 변경                         |
| 📝 Docs        | md, yml 등의 문서 작업                               |
| 🔧 Chore       | 이외의 애매하거나 자잘한 수정                            |
| ⚙️ Setting     | 빌드 및 패키지 등 프로젝트 설정                           |

```
< Issue Title >
[Issue_종류] 구현_주요내용
ex) Feat: 소셜 로그인 및 회원가입 기능
```

### Commit
```
< Commit Message >
[#Issue_번호] Issue_종류: 구현_내용
ex) [#32] Feat: Security 및 OAuth2 구현
```

### Pull Request
- Pull Request만 날리고, 중요 Approve는 reviewer가 한다.
- PR Open & Review & Close(CI/CD) &#8594; Slack 알림
```
< PR Title >
[#Issue_번호] Issue_종류: 이슈내용
ex) [#32] Feat: 소셜 로그인 및 회원가입 기능
```

### Template
- <a href="https://github.com/Dev-Race/DevRace-backend/tree/develop/.github/ISSUE_TEMPLATE">Issue Template</a>
- <a href="https://github.com/Dev-Race/DevRace-backend/blob/develop/.github/PULL_REQUEST_TEMPLATE.md">Pull-Request Template</a>
<br>



## 📂 Package Convention

### Structure
```
## DevOps ##
├── .ebextensions : AWS EB 환경 설정
├── .github
│   └── workflows : CI/CD 실행, Slack 알림
├── .gitmodules : Git 서브모듈 정의
├── .platform
│   └── hooks
│       └── postdeploy : EB 배포 이후 실행
├── Dockerfile : 도커 이미지 빌드 설정
├── Dockerrun.aws.json : 도커 컨테이너 EB 배포 설정
└── submodule-backend : 배포용 properties 관리

+

## Backend ##
├── config
├── controller
├── service
│   └── impl
├── repository
├── domain : Entity
│   ├── enums
│   ├── common : BaseEntity
│   └── mapping : 다대다 매핑
├── dto
├── response : API 응답, Exception 핸들러
│   ├── responseitem
│   └── exception
├── security
│   ├── jwt : 토큰 처리
│   │   └── handler
│   └── oauth2 : 소셜 로그인
│       ├── handler
│       └── userinfo
└── util
```
<details>
  <summary>&nbsp;<strong>Detailed Structure</strong>&nbsp;:&nbsp;Open!</summary>
  <br>

```
├── .ebextensions
│   ├── 00-set-timezone.config
│   ├── 01-set-swapmemory.config
│   └── 02-rabbitmq.config
├── .github
│   ├── ISSUE_TEMPLATE
│   │   ├── custom.md
│   │   ├── feature.md
│   │   ├── fix.md
│   │   └── refactor.md
│   ├── PULL_REQUEST_TEMPLATE.md
│   └── workflows
│       ├── deploy.yml
│       ├── slack-pr-open.yml
│       └── slack-pr-review.yml
├── .gitmodules
├── .platform
│   └── hooks
│       └── postdeploy
│           └── connect_spring_to_network.sh
├── Dockerfile
├── Dockerrun.aws.json
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── sajang
│   │   │           └── devracebackend
│   │   │               ├── DevraceBackendApplication.java
│   │   │               ├── config
│   │   │               │   ├── AwsS3Config.java
│   │   │               │   ├── RabbitConfig.java
│   │   │               │   ├── SecurityConfig.java
│   │   │               │   ├── StompConfig.java
│   │   │               │   └── SwaggerConfig.java
│   │   │               ├── controller
│   │   │               │   ├── AuthController.java
│   │   │               │   ├── ChatController.java
│   │   │               │   ├── RoomController.java
│   │   │               │   ├── TestController.java
│   │   │               │   └── UserController.java
│   │   │               ├── domain
│   │   │               │   ├── Chat.java
│   │   │               │   ├── Problem.java
│   │   │               │   ├── Room.java
│   │   │               │   ├── User.java
│   │   │               │   ├── common
│   │   │               │   │   └── BaseEntity.java
│   │   │               │   ├── enums
│   │   │               │   │   ├── Language.java
│   │   │               │   │   ├── MessageType.java
│   │   │               │   │   ├── Role.java
│   │   │               │   │   ├── RoomState.java
│   │   │               │   │   └── SocialType.java
│   │   │               │   └── mapping
│   │   │               │       └── UserRoom.java
│   │   │               ├── dto
│   │   │               │   ├── AuthDto.java
│   │   │               │   ├── ChatDto.java
│   │   │               │   ├── ProblemDto.java
│   │   │               │   ├── RoomDto.java
│   │   │               │   ├── UserDto.java
│   │   │               │   └── UserRoomDto.java
│   │   │               ├── repository
│   │   │               │   ├── ChatRepository.java
│   │   │               │   ├── ProblemRepository.java
│   │   │               │   ├── RoomRepository.java
│   │   │               │   ├── UserRepository.java
│   │   │               │   ├── UserRoomRepository.java
│   │   │               │   └── UserRoomBatchRepository.java
│   │   │               ├── response
│   │   │               │   ├── GlobalExceptionHandler.java
│   │   │               │   ├── ResponseCode.java
│   │   │               │   ├── ResponseData.java
│   │   │               │   ├── exception
│   │   │               │   │   ├── CustomException.java
│   │   │               │   │   ├── Exception400.java
│   │   │               │   │   ├── Exception404.java
│   │   │               │   │   └── Exception500.java
│   │   │               │   └── responseitem
│   │   │               │       ├── MessageItem.java
│   │   │               │       └── StatusItem.java
│   │   │               ├── security
│   │   │               │   ├── jwt
│   │   │               │   │   ├── JwtChannelInterceptor.java
│   │   │               │   │   ├── JwtFilter.java
│   │   │               │   │   ├── TokenProvider.java
│   │   │               │   │   └── handler
│   │   │               │   │       ├── JwtAccessDeniedHandler.java
│   │   │               │   │       ├── JwtAuthenticationEntryPoint.java
│   │   │               │   │       ├── JwtExceptionFilter.java
│   │   │               │   │       └── JwtStompExceptionHandler.java
│   │   │               │   └── oauth2
│   │   │               │       ├── CustomOAuth2User.java
│   │   │               │       ├── CustomOAuth2UserService.java
│   │   │               │       ├── OAuthAttributes.java
│   │   │               │       ├── handler
│   │   │               │       │   ├── OAuth2LoginFailureHandler.java
│   │   │               │       │   └── OAuth2LoginSuccessHandler.java
│   │   │               │       └── userinfo
│   │   │               │           ├── GithubOAuth2UserInfo.java
│   │   │               │           ├── GoogleOAuth2UserInfo.java
│   │   │               │           └── OAuth2UserInfo.java
│   │   │               ├── service
│   │   │               │   ├── AuthService.java
│   │   │               │   ├── AwsS3Service.java
│   │   │               │   ├── ChatService.java
│   │   │               │   ├── ProblemService.java
│   │   │               │   ├── RoomService.java
│   │   │               │   ├── UserRoomService.java
│   │   │               │   ├── UserService.java
│   │   │               │   └── impl
│   │   │               │       ├── AuthServiceImpl.java
│   │   │               │       ├── AwsS3ServiceImpl.java
│   │   │               │       ├── ChatServiceImpl.java
│   │   │               │       ├── ProblemServiceImpl.java
│   │   │               │       ├── RoomServiceImpl.java
│   │   │               │       ├── UserRoomServiceImpl.java
│   │   │               │       └── UserServiceImpl.java
│   │   │               └── util
│   │   │                   ├── LongListConverter.java
│   │   │                   ├── MultipartJackson2HttpMessageConverter.java
│   │   │                   ├── SecurityUtil.java
│   │   │                   └── StringListConverter.java
│   │   └── resources
│   │       ├── application-local.properties
│   │       ├── application-secret.properties
│   │       └── application.properties
└── submodule-backend
    ├── application-prod.properties
    └── application-secret.properties
```
</details>
<br>



## 👨‍👩‍👧‍👧 Team
| [사현진](https://github.com/tkguswls1106) | [장준상](https://github.com/JunSang1121) | [정연재](https://github.com/zzangjyj0818) | [권인우](https://github.com/inwoo0206) | [이정향](https://github.com/Hyanggggg) |
| :----------------------------------------: | :----------------------------------------: | :----------------------------------------: | :----------------------------------------: | :----------------------------------------: |
| <img width = "300" src ="https://github.com/Dev-Race/DevRace-backend/assets/56509933/20a67908-ca87-49ea-aafc-60f306302429"><br>- Team Leader - | <img width = "300" src ="https://github.com/Dev-Race/DevRace-backend/assets/56509933/2223bac3-48ed-4a98-9f0f-6bd860a080ac"> | <img width = "300" src ="https://github.com/Dev-Race/DevRace-backend/assets/56509933/798cc442-77f0-4b81-a61e-56aaedf784d5"> | <img width = "300" src ="https://github.com/Dev-Race/DevRace-backend/assets/56509933/79fe6476-58ae-42ce-83ca-2dd6a59c4585"> | <img width = "300" src ="https://github.com/Dev-Race/DevRace-backend/assets/56509933/e9ef0c57-73cb-4b2c-9b61-f9773fb02f5b"> |
| Project Manager,<br>DevOps Engineer,<br>BE Developer | Backend Developer | Frontend Developer | Frontend Developer | Designer |
