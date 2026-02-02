# 인증 블로그 v2

## 1. 기술스택

- session, cookie
- orm
- lazy loading
- response(응답) DTO (왜 필요한지?)
- Optional, Stream API(map, filter, 어부(물가, 가공, 수집))
- 권한(403)과 인증(401)

## 2. 리팩토링

- ResponseDTO 내부클래스로 수정

## 3. 기능

- 회원가입 (아이디 중복체크)
- 로그인 (쿠키)
- 게시글쓰기(인증이 된 사람 - 수정)
- 게시글상세보기(인증/권한체크, DTO만들기 - 수정)
- 게시글수정/삭제 (인증/권한체크 - 수정)

## 4. Task

### 1. 회원가입

- 그림 다운로드 (v)
- user폴더 UserController 만들어서 그림 연결 (v)
- User 테이블 생성 - 더미데이터 (v)
- UserRepository만들어서 DB 테스트 코드 (v)
- 컨트롤러, 서비스, 레포 연결해서 기능 완료하기 (v)
