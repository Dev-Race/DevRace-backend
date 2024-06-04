<br>
<p align="center">
  <img src="https://github.com/Dev-Race/.github/assets/56509933/d3c424bc-4cc9-4c44-ad1c-05f6e251d133" />
</p>

<p align="center">
  <strong>
    - Dev Race 🧑‍💻 -
    <br>
    실시간 코딩 경쟁 및 채팅 서비스
  </strong>
</p>
<br><br>


## 📄 Documents

### [PM]&nbsp;&nbsp;기획 명세서
<details>
  <summary>&nbsp;<a href="https://sahyunjin.notion.site/7384e0322e8d480c8f639ae1a84915fa?v=eda603b2a8844e6fa244492f502ba129&pvs=4">기능 상세 명세서</a></summary>
  <br><img width="1124" alt="DevRace_기능상세_명세서" src="https://github.com/tkguswls1106/DevRace-Readme/assets/56509933/38c36139-1d4a-4075-bdf7-6ca33f222466">
</details>
<details>
  <summary>&nbsp;<a href="https://docs.google.com/spreadsheets/d/18bLgLlZGMPsulnnqDtMa3JtHX4kLwSqaXHfTSW5EAvw/edit?usp=sharing">WBS 명세서</a></summary>
  <br><img width="1124" alt="DevRace_WBS_명세서" src="https://github.com/tkguswls1106/DevRace-Readme/assets/56509933/3f3cc0aa-d485-4762-9850-f34ad1a8e98f">
</details>

### [BE]&nbsp;&nbsp;API 명세서
<details>
  <summary>&nbsp;<a href="https://sahyunjin.notion.site/2071d1695b254b78a1367ef555d6b820?v=336213c0a3b345f28fdb4ef181044d31&pvs=4">Rest API 명세서</a></summary>
  <br><img width="1124" alt="DevRace_RestAPI_명세서" src="https://github.com/tkguswls1106/DevRace-Readme/assets/56509933/863cd935-c292-4318-97e2-03b357528a27">
</details>
<details>
  <summary>&nbsp;<a href="https://sahyunjin.notion.site/2e577f50c85648cdaae86aeeae66be5a?v=a717219fcb494da39e765bea246b6cfd&pvs=4">WebSocket API 명세서</a></summary>
  <br><img width="1124" alt="DevRace_WebSocketAPI_명세서" src="https://github.com/tkguswls1106/DevRace-Readme/assets/56509933/a5e52b5a-c3af-405c-89a7-43c24e5f1182">
</details>
<details open>
  <summary>&nbsp;Swagger API 명세서</summary>
  <br><img src="https://github.com/tkguswls1106/DevRace-Readme/assets/56509933/222b7599-69ba-41aa-9ef5-613fd7837c54" />
</details>
<br>


## 💻 Architecture

### Entire Architecture
![devrace_architecture drawio](https://github.com/tkguswls1106/DevRace-Readme/assets/56509933/bafcab1e-77b4-4e9d-9db2-aac15c130eed)

### Network Architecture
![devrace_network_architecture drawio](https://github.com/tkguswls1106/DevRace-Readme/assets/56509933/9918f3c3-f0b5-4eb2-a712-19c665737576)
<br><br>


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
<br>


## 🗂️ Database
![devrace DB ERD](https://github.com/tkguswls1106/DevRace-Readme/assets/56509933/4b0db7b7-d530-4acf-9361-c1689d48f97a)
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
├── response : API 응답값, Exception 핸들러
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
<br>


## 👨‍👩‍👧‍👧 Team
| [사현진](https://github.com/tkguswls1106) | [장준상](https://github.com/JunSang1121) | [정연재](https://github.com/zzangjyj0818) | [권인우](https://github.com/inwoo0206) | [이정향](https://github.com/Hyanggggg) |
| :----------------------------------------: | :----------------------------------------: | :----------------------------------------: | :----------------------------------------: | :----------------------------------------: |
| <img width = "300" src ="https://github.com/tkguswls1106/DevRace-Readme/assets/56509933/6f1a9df8-8511-466c-b22e-93c55f9919ac"><br>- Team Leader - | <img width = "300" src ="https://github.com/tkguswls1106/DevRace-Readme/assets/56509933/f872cd42-cfa2-4818-a717-75451450382e"> | <img width = "300" src ="https://github.com/tkguswls1106/DevRace-Readme/assets/56509933/3794fbbb-20d5-4752-956e-4560bd15e1f0"> | <img width = "300" src ="https://github.com/tkguswls1106/DevRace-Readme/assets/56509933/866c8eac-3b18-453d-8e33-e5fa06817a89"> | <img width = "300" src ="https://github.com/2023-Hackathon-TeamSMUD/.github/assets/56509933/a563b39b-6d80-40b1-930f-311455b21e82"> |
| Project Manager,<br>DevOps Engineer,<br>BE Developer | Backend Developer | Frontend Developer | Frontend Developer | Designer |
