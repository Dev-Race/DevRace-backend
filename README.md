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