# Spring Boot 게시판 만들기 - 실습 가이드북

> Spring Boot 4 + JPA + Mustache로 게시판을 처음부터 끝까지 만들어보는 실습 교재

---

## 전체 구조도

```mermaid
graph TB
    subgraph Client["브라우저 (Client)"]
        HTML["Mustache 템플릿<br/>HTML + Bootstrap"]
    end

    subgraph Controller["Controller 계층"]
        UC[UserController]
        BC[BoardController]
        RC[ReplyController]
    end

    subgraph Service["Service 계층"]
        US[UserService]
        BS[BoardService]
        RS[ReplyService]
    end

    subgraph Repository["Repository 계층"]
        UR[UserRepository]
        BR[BoardRepository]
        RR[ReplyRepository]
    end

    subgraph DB["데이터베이스 (H2)"]
        UT[(user_tb)]
        BT[(board_tb)]
        RT[(reply_tb)]
    end

    subgraph Exception["예외 처리"]
        GEH[GlobalExceptionHandler]
        E4["Exception 400/401/403/404/500"]
    end

    HTML -->|HTTP 요청| Controller
    Controller -->|비즈니스 로직 위임| Service
    Service -->|데이터 접근| Repository
    Repository -->|SQL| DB
    Controller -.->|예외 발생| GEH
    Service -.->|예외 발생| GEH
    GEH --> E4
```

---

## 목차

| 챕터 | 제목 | 핵심 내용 |
|------|------|----------|
| [Chapter 01](ch01-project-setup.md) | 프로젝트 소개와 환경 설정 | Spring Boot, Gradle, H2, application.properties |
| [Chapter 02](ch02-entity.md) | 엔티티(Entity) 설계 | User, Board, Reply 테이블 + 연관관계 매핑 |
| [Chapter 03](ch03-repository.md) | Repository 계층 | EntityManager, JPQL, 영속성 컨텍스트 |
| [Chapter 04](ch04-user.md) | 회원가입과 로그인 | UserService, UserController, HttpSession |
| [Chapter 05](ch05-board-crud.md) | 게시글 CRUD | 목록, 상세, 작성, 수정, 삭제 |
| [Chapter 06](ch06-reply.md) | 댓글 기능 | 댓글 등록, 삭제, getReference |
| [Chapter 07](ch07-exception.md) | 예외 처리 | 커스텀 예외, GlobalExceptionHandler |
| [Chapter 08](ch08-view.md) | 뷰(Mustache) 템플릿 | header, index, form, detail |
| [Chapter 09](ch09-test.md) | 테스트 코드 | @DataJpaTest, Repository 테스트 |

---

## 기술 스택

| 기술 | 버전 | 설명 |
|------|------|------|
| Java | 21 | 프로그래밍 언어 |
| Spring Boot | 4.0.2 | 웹 프레임워크 |
| Spring Data JPA | - | ORM (Hibernate) |
| H2 Database | - | 인메모리 데이터베이스 |
| Mustache | - | 서버사이드 템플릿 엔진 |
| Lombok | - | 보일러플레이트 코드 제거 |
| Bootstrap | 5.3.3 | CSS 프레임워크 |
| Gradle | 9.2.1 | 빌드 도구 |

---

## 프로젝트 패키지 구조

```
src/main/java/com/example/boardv1/
├── Boardv1Application.java          # 메인 클래스
├── _core/
│   └── errors/
│       ├── GlobalExceptionHandler.java
│       └── ex/
│           ├── Exception400.java     # 유효성검사 실패
│           ├── Exception401.java     # 인증 실패
│           ├── Exception403.java     # 권한 실패
│           ├── Exception404.java     # 자원 없음
│           └── Exception500.java     # 서버 에러
├── user/
│   ├── User.java                     # 엔티티
│   ├── UserController.java           # 컨트롤러
│   ├── UserService.java              # 서비스
│   ├── UserRepository.java           # 레포지토리
│   ├── UserRequest.java              # 요청 DTO
│   └── UserResponse.java             # 응답 DTO
├── board/
│   ├── Board.java                    # 엔티티
│   ├── BoardController.java          # 컨트롤러
│   ├── BoardService.java             # 서비스
│   ├── BoardRepository.java          # 레포지토리
│   ├── BoardRequest.java             # 요청 DTO
│   └── BoardResponse.java            # 응답 DTO
└── reply/
    ├── Reply.java                    # 엔티티
    ├── ReplyController.java          # 컨트롤러
    ├── ReplyService.java             # 서비스
    ├── ReplyRepository.java          # 레포지토리
    ├── ReplyRequest.java             # 요청 DTO
    └── ReplyResponse.java            # 응답 DTO

src/main/resources/
├── application.properties            # 설정 파일
├── db/
│   └── data.sql                      # 초기 데이터
└── templates/
    ├── header.mustache               # 공통 헤더
    ├── index.mustache                # 메인 페이지
    ├── user/
    │   ├── login-form.mustache       # 로그인 폼
    │   └── join-form.mustache        # 회원가입 폼
    └── board/
        ├── detail.mustache           # 게시글 상세
        ├── save-form.mustache        # 게시글 작성 폼
        └── update-form.mustache      # 게시글 수정 폼
```
