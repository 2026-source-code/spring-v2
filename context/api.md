# API 명세서 (boardv1 프로젝트)

> 게시판 프로젝트의 모든 API 엔드포인트를 정리한 문서입니다.

---

## 목차

- [1. Board API (게시글)](#1-board-api-게시글)
- [2. User API (사용자)](#2-user-api-사용자)
- [3. Reply API (댓글)](#3-reply-api-댓글)
- [4. 인증/인가 정리](#4-인증인가-정리)
- [5. 예외 처리](#5-예외-처리)

---

## 1. Board API (게시글)

### 1.1 게시글 목록 조회 (메인 페이지)

```
GET /
```

**설명:** 전체 게시글 목록을 조회합니다.

**인증/인가:**
- 인증 불필요 (누구나 접근 가능)

**응답:**
- View: `index.mustache`
- Model: `models` (List<Board>)

**예시:**
```
GET http://localhost:8080/
```

---

### 1.2 게시글 상세 조회

```
GET /boards/{id}
```

**설명:** 특정 게시글의 상세 정보를 조회합니다. (작성자, 내용, 댓글 포함)

**인증/인가:**
- 인증 불필요 (비로그인 사용자도 조회 가능)
- 로그인 시: 수정/삭제 버튼 표시 여부가 `isOwner`로 결정됨

**경로 변수:**
- `id` (int): 게시글 ID

**응답:**
- View: `board/detail.mustache`
- Model: `model` (BoardResponse.DetailDTO)
  - `id`: 게시글 ID
  - `title`: 제목
  - `content`: 내용
  - `username`: 작성자 이름
  - `isOwner`: 내 글인지 여부 (boolean)
  - `replies`: 댓글 목록 (List<ReplyResponse.DTO>)

**예시:**
```
GET http://localhost:8080/boards/1
```

---

### 1.3 게시글 상세 조회 (API)

```
GET /api/boards/{id}
```

**설명:** JSON 형식으로 게시글 상세 정보를 반환합니다.

**인증/인가:**
- 인증 불필요

**경로 변수:**
- `id` (int): 게시글 ID

**응답:**
- Content-Type: `application/json`
- Body: `BoardResponse.DetailDTO` (JSON)

**예시:**
```
GET http://localhost:8080/api/boards/1

응답 예시:
{
  "id": 1,
  "userId": 1,
  "title": "제목1",
  "content": "내용1",
  "username": "ssar",
  "isOwner": true,
  "replies": [...]
}
```

---

### 1.4 게시글 작성 폼

```
GET /boards/save-form
```

**설명:** 게시글 작성 페이지를 보여줍니다.

**인증/인가:**
- 인증 필요 (로그인 필수)
- 비로그인 시: `Exception401` 발생 → 로그인 페이지로 이동

**응답:**
- View: `board/save-form.mustache`

**예시:**
```
GET http://localhost:8080/boards/save-form
```

---

### 1.5 게시글 작성

```
POST /boards/save
```

**설명:** 새 게시글을 작성합니다.

**인증/인가:**
- 인증 필요 (로그인 필수)
- 비로그인 시: `Exception401` 발생

**요청 Body (x-www-form-urlencoded):**
- `title` (String, 필수): 게시글 제목
  - Validation: `@NotBlank`, `@Size(max=100)`
- `content` (String, 필수): 게시글 내용
  - Validation: `@NotBlank`

**응답:**
- 성공 시: `redirect:/` (메인 페이지로 이동)

**예시:**
```
POST http://localhost:8080/boards/save
Content-Type: application/x-www-form-urlencoded

title=좋은 글&content=유익한 내용입니다
```

**오류:**
- 제목 비어있음 → `Exception400`: "제목을 입력해주세요"
- 내용 비어있음 → `Exception400`: "내용을 입력해주세요"
- 제목 100자 초과 → `Exception400`: "제목은 100자 이내로 입력해주세요"

---

### 1.6 게시글 수정 폼

```
GET /boards/{id}/update-form
```

**설명:** 게시글 수정 페이지를 보여줍니다.

**인증/인가:**
- 인증 필요
- 권한 필요: **본인 글만** 수정 가능
  - 타인 글 접근 시: `Exception403` 발생

**경로 변수:**
- `id` (int): 게시글 ID

**응답:**
- View: `board/update-form.mustache`
- Model: `model` (Board)

**예시:**
```
GET http://localhost:8080/boards/1/update-form
```

**오류:**
- 게시글 없음 → `Exception404`: "게시글을 찾을 수 없어요"
- 권한 없음 → `Exception403`: "수정할 권한이 없습니다"

---

### 1.7 게시글 수정

```
POST /boards/{id}/update
```

**설명:** 게시글을 수정합니다.

**인증/인가:**
- 인증 필요
- 권한 필요: **본인 글만** 수정 가능

**경로 변수:**
- `id` (int): 게시글 ID

**요청 Body (x-www-form-urlencoded):**
- `title` (String, 필수): 수정할 제목
  - Validation: `@NotBlank`, `@Size(max=100)`
- `content` (String, 필수): 수정할 내용
  - Validation: `@NotBlank`

**응답:**
- 성공 시: `redirect:/boards/{id}` (해당 게시글 상세 페이지로)

**예시:**
```
POST http://localhost:8080/boards/1/update
Content-Type: application/x-www-form-urlencoded

title=수정된 제목&content=수정된 내용
```

**오류:**
- 게시글 없음 → `Exception404`: "수정할 게시글을 찾을 수 없어요"
- 권한 없음 → `Exception403`: "수정할 권한이 없습니다"

---

### 1.8 게시글 삭제

```
POST /boards/{id}/delete
```

**설명:** 게시글을 삭제합니다.

**인증/인가:**
- 인증 필요
- 권한 필요: **본인 글만** 삭제 가능

**경로 변수:**
- `id` (int): 게시글 ID

**응답:**
- 성공 시: `redirect:/` (메인 페이지로)

**예시:**
```
POST http://localhost:8080/boards/1/delete
```

**오류:**
- 게시글 없음 → `Exception404`: "삭제할 게시글을 찾을 수 없어요"
- 권한 없음 → `Exception403`: "삭제할 권한이 없습니다"
- 댓글 있는 글 → `Exception500`: "댓글이 있는 게시글을 삭제할 수 없습니다"

---

## 2. User API (사용자)

### 2.1 회원가입 폼

```
GET /join-form
```

**설명:** 회원가입 페이지를 보여줍니다.

**인증/인가:**
- 인증 불필요

**응답:**
- View: `user/join-form.mustache`

**예시:**
```
GET http://localhost:8080/join-form
```

---

### 2.2 회원가입

```
POST /join
```

**설명:** 새로운 사용자를 등록합니다.

**인증/인가:**
- 인증 불필요

**요청 Body (x-www-form-urlencoded):**
- `username` (String, 필수): 아이디
  - Validation: `@NotBlank`, `@Size(min=3, max=20)`
- `password` (String, 필수): 비밀번호
  - Validation: `@NotBlank`, `@Size(min=4, max=20)`
- `email` (String, 선택): 이메일

**응답:**
- 성공 시: `redirect:/login-form` (로그인 페이지로)

**예시:**
```
POST http://localhost:8080/join
Content-Type: application/x-www-form-urlencoded

username=newuser&password=1234&email=new@test.com
```

**오류:**
- username 중복 → `Exception400`: "유저네임이 중복되었습니다"
- username 3자 미만 → `Exception400`: "유저네임은 3~20자로 입력해주세요"
- password 4자 미만 → `Exception400`: "비밀번호는 4~20자로 입력해주세요"

---

### 2.3 로그인 폼

```
GET /login-form
```

**설명:** 로그인 페이지를 보여줍니다.

**인증/인가:**
- 인증 불필요

**응답:**
- View: `user/login-form.mustache`

**예시:**
```
GET http://localhost:8080/login-form
```

---

### 2.4 로그인

```
POST /login
```

**설명:** 사용자 인증을 수행하고 세션을 생성합니다.

**인증/인가:**
- 인증 불필요

**요청 Body (x-www-form-urlencoded):**
- `username` (String, 필수): 아이디
  - Validation: `@NotBlank`
- `password` (String, 필수): 비밀번호
  - Validation: `@NotBlank`

**응답:**
- 성공 시:
  - Session에 `sessionUser` 저장
  - Cookie에 `username` 저장
  - `redirect:/` (메인 페이지로)

**예시:**
```
POST http://localhost:8080/login
Content-Type: application/x-www-form-urlencoded

username=ssar&password=1234
```

**오류:**
- username 없음 → `Exception404`: "username을 찾을 수 없어요"
- password 불일치 → `Exception401`: "패스워드가 일치하지 않아요"

---

### 2.5 로그아웃

```
GET /logout
```

**설명:** 세션을 무효화하고 로그아웃합니다.

**인증/인가:**
- 인증 필요 (암묵적)

**응답:**
- 성공 시: `redirect:/` (메인 페이지로)

**예시:**
```
GET http://localhost:8080/logout
```

---

## 3. Reply API (댓글)

### 3.1 댓글 작성

```
POST /replies/save
```

**설명:** 게시글에 댓글을 작성합니다.

**인증/인가:**
- 인증 필요 (로그인 필수)
- 비로그인 시: `Exception401` 발생

**요청 Body (x-www-form-urlencoded):**
- `boardId` (Integer, 필수): 게시글 ID
  - Validation: `@NotNull`
- `comment` (String, 필수): 댓글 내용
  - Validation: `@NotBlank`

**응답:**
- 성공 시: `redirect:/boards/{boardId}` (해당 게시글 상세 페이지로)

**예시:**
```
POST http://localhost:8080/replies/save
Content-Type: application/x-www-form-urlencoded

boardId=6&comment=좋은 글이네요!
```

**오류:**
- 댓글 내용 비어있음 → `Exception400`: "댓글을 입력해주세요"
- boardId null → `Exception400`: "게시글 ID가 필요합니다"

---

### 3.2 댓글 삭제

```
POST /replies/{id}/delete?boardId={boardId}
```

**설명:** 댓글을 삭제합니다.

**인증/인가:**
- 인증 필요
- 권한 필요: **본인 댓글만** 삭제 가능

**경로 변수:**
- `id` (int): 댓글 ID

**쿼리 파라미터:**
- `boardId` (int): 게시글 ID (삭제 후 리다이렉트용)

**응답:**
- 성공 시: `redirect:/boards/{boardId}` (해당 게시글 상세 페이지로)

**예시:**
```
POST http://localhost:8080/replies/5/delete?boardId=6
```

**오류:**
- 댓글 없음 → `Exception404`: "댓글을 찾을 수 없습니다"
- 권한 없음 → `Exception403`: "댓글을 삭제할 권한이 없습니다"

---

## 4. 인증/인가 정리

### 4.1 인증 (Authentication) - "너 누구야?"

| 엔드포인트 | 인증 필요 여부 | 실패 시 예외 |
|-----------|-------------|------------|
| `GET /` | ❌ 불필요 | - |
| `GET /boards/{id}` | ❌ 불필요 | - |
| `GET /api/boards/{id}` | ❌ 불필요 | - |
| `GET /boards/save-form` | ✅ 필요 | Exception401 |
| `POST /boards/save` | ✅ 필요 | Exception401 |
| `GET /boards/{id}/update-form` | ✅ 필요 | Exception401 |
| `POST /boards/{id}/update` | ✅ 필요 | Exception401 |
| `POST /boards/{id}/delete` | ✅ 필요 | Exception401 |
| `GET /join-form` | ❌ 불필요 | - |
| `POST /join` | ❌ 불필요 | - |
| `GET /login-form` | ❌ 불필요 | - |
| `POST /login` | ❌ 불필요 | - |
| `GET /logout` | ✅ 필요 | - |
| `POST /replies/save` | ✅ 필요 | Exception401 |
| `POST /replies/{id}/delete` | ✅ 필요 | Exception401 |

### 4.2 권한 (Authorization) - "이거 할 수 있어?"

| 엔드포인트 | 권한 체크 | 조건 | 실패 시 예외 |
|-----------|---------|------|------------|
| `GET /boards/{id}/update-form` | ✅ 있음 | 본인 글만 | Exception403 |
| `POST /boards/{id}/update` | ✅ 있음 | 본인 글만 | Exception403 |
| `POST /boards/{id}/delete` | ✅ 있음 | 본인 글만 | Exception403 |
| `POST /replies/{id}/delete` | ✅ 있음 | 본인 댓글만 | Exception403 |
| 기타 모든 엔드포인트 | ❌ 없음 | - | - |

### 4.3 세션 관리

**세션에 저장되는 데이터:**
- `sessionUser` (User 객체): 로그인한 사용자 정보

**세션 생성:**
- `POST /login` 성공 시 `session.setAttribute("sessionUser", user)` 실행

**세션 삭제:**
- `GET /logout` 시 `session.invalidate()` 실행

**세션 확인:**
- 인증이 필요한 엔드포인트에서 `session.getAttribute("sessionUser")` 체크

---

## 5. 예외 처리

### 5.1 커스텀 예외 종류

| 예외 클래스 | HTTP 상태 | 의미 | 발생 시점 |
|-----------|----------|------|----------|
| `Exception400` | 400 Bad Request | 유효성 검사 실패 / 중복 | @Valid 실패, username 중복 |
| `Exception401` | 401 Unauthorized | 인증 실패 | 로그인 안 함, 비밀번호 불일치 |
| `Exception403` | 403 Forbidden | 권한 없음 | 타인 글/댓글 수정/삭제 시도 |
| `Exception404` | 404 Not Found | 리소스 없음 | 게시글/댓글/사용자 없음 |
| `Exception500` | 500 Server Error | 서버 오류 | 댓글 있는 글 삭제 등 |

### 5.2 GlobalExceptionHandler 처리

모든 예외는 `GlobalExceptionHandler`에서 처리되며, 다음과 같이 응답됩니다:

```html
<script>
    alert('에러 메시지');
    history.back();  // 또는 location.href = '/login-form' (Exception401만)
</script>
```

**Exception401의 특별한 처리:**
- 다른 예외: `history.back()` (이전 페이지로)
- Exception401: `location.href = '/login-form'` (로그인 페이지로)

### 5.3 유효성 검사 (ValidationHandler AOP)

`@PostMapping`이 붙은 모든 엔드포인트는 **AOP**로 자동 유효성 검사가 실행됩니다.

**검사 시점:** 메서드 실행 **전** (`@Before`)

**검사 대상:** `@Valid` + `Errors` 파라미터가 있는 메서드

**실패 시:** `Exception400` 발생 → GlobalExceptionHandler 처리

---

## 6. 데이터 흐름 예시

### 6.1 게시글 작성 성공 흐름

```
1. 사용자: POST /boards/save (title="좋은 글", content="내용")
2. ValidationHandler (AOP): @Valid 검사 → 통과!
3. BoardController.save():
   - session.getAttribute("sessionUser") → ssar (있음!)
   - boardService.게시글쓰기(...) 호출
4. BoardService.게시글쓰기():
   - Board 엔티티 생성
   - boardRepository.save(board)
   - DB INSERT 쿼리 실행
5. Controller: return "redirect:/"
6. 사용자: 메인 페이지로 리다이렉트 (새 글 추가됨)
```

### 6.2 게시글 삭제 실패 흐름 (권한 없음)

```
1. 사용자 (cos): POST /boards/1/delete
2. BoardController.delete():
   - session.getAttribute("sessionUser") → cos (있음, 인증 통과)
   - boardService.게시글삭제(id=1, sessionUserId=2) 호출
3. BoardService.게시글삭제():
   - Board 1번 조회 → user_id = 1 (ssar)
   - sessionUserId(2) != board.userId(1) → 권한 없음!
   - throw new Exception403("삭제할 권한이 없습니다")
4. GlobalExceptionHandler.ex403():
   - alert('삭제할 권한이 없습니다')
   - history.back()
5. 사용자: 이전 페이지로 돌아감 (알림창 표시)
```

---

## 7. 기술 스택

- **Framework:** Spring Boot 4.0.2
- **Java:** 21
- **Database:** H2 (in-memory)
- **Template Engine:** Mustache
- **ORM:** JPA/Hibernate
- **Build Tool:** Gradle
- **Session Management:** HttpSession
- **Validation:** Spring Validation (Bean Validation)
- **AOP:** Spring AOP (AspectJ)

---

## 8. 참고사항

### 8.1 N+1 문제 해결

`application.properties`에 다음 설정이 적용되어 있습니다:

```properties
spring.jpa.properties.hibernate.default_batch_fetch_size=10
```

**효과:** 연관 데이터를 개별 SELECT 대신 **IN 쿼리**로 일괄 조회하여 성능 향상

### 8.2 OSIV 설정

```properties
spring.jpa.open-in-view=false
```

**의미:** Service 계층에서 모든 데이터를 DTO로 준비해서 Controller에 전달

### 8.3 data.sql 초기 데이터

애플리케이션 시작 시 `src/main/resources/db/data.sql`의 데이터가 자동 로드됩니다:

- User 2명 (ssar, cos)
- Board 6개 (ssar 3개, cos 3개)
- Reply 5개 (Board 5번, 6번에 댓글)

---

## 9. 테스트 방법

### Postman / cURL 예시

**로그인:**
```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=ssar&password=1234" \
  -c cookies.txt
```

**게시글 작성 (쿠키 포함):**
```bash
curl -X POST http://localhost:8080/boards/save \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "title=테스트 글&content=테스트 내용" \
  -b cookies.txt
```

**API로 게시글 조회:**
```bash
curl -X GET http://localhost:8080/api/boards/1
```

---

## 10. 연락처

프로젝트 관련 문의:
- GitHub: [프로젝트 저장소 URL]
- 이메일: [이메일 주소]

---

**문서 버전:** 1.0
**최종 수정일:** 2026-02-09
**작성자:** Claude Code
