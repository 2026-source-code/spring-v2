# oh-my-claudecode 분석 문서

> GitHub: https://github.com/Yeachan-Heo/oh-my-claudecode

## 개요

Claude Code를 위한 **다중 에이전트 오케스트레이션 플러그인**. 자연어 키워드만으로 복잡한 개발 작업을 자동화하며, 별도의 학습 곡선 없이 즉시 사용할 수 있도록 설계되었다.

- Stars: 4,900+
- Forks: 348
- Contributors: 21명
- License: MIT

## 기술 스택

| 언어 | 비율 |
|------|------|
| TypeScript | 57.8% |
| JavaScript | 38.6% |
| Python | 1.9% |
| Shell | 1.7% |

## 요구사항

- Claude Code CLI (터미널에서 `claude` 명령어 실행 가능해야 함)
- Claude Max/Pro 구독 또는 Anthropic API 키
- (선택) tmux - 레이트 제한 대기 기능 사용 시
- (선택) Gemini CLI, Codex CLI - 외부 AI 오케스트레이션용

---

## 설치 및 설정 가이드

### Step 1. 플러그인 설치

Claude Code 터미널에서 아래 명령어를 순서대로 입력한다:

```bash
# 마켓플레이스에 추가
/plugin marketplace add https://github.com/Yeachan-Heo/oh-my-claudecode

# 설치
/plugin install oh-my-claudecode
```

### Step 2. 초기 설정 (omc-setup)

```bash
/oh-my-claudecode:omc-setup
```

이 명령을 실행하면:
- `./.claude/CLAUDE.md` 파일이 생성됨 (프로젝트별 설정)
- 에이전트, 스킬, 훅 시스템이 자동 구성됨
- 해당 프로젝트에서만 적용됨

> 전역 설정을 원하면 `~/.claude/CLAUDE.md`에 수동으로 설정할 수 있다. 프로젝트 설정이 글로벌 설정보다 우선한다.

### Step 3. 사용 시작

설정 완료 후 바로 자연어로 사용 가능:

```bash
autopilot: build a todo app
```

### 업데이트 방법

```bash
/plugin install oh-my-claudecode
/oh-my-claudecode:omc-setup
```

### 문제 발생 시 진단

```bash
/oh-my-claudecode:doctor
```

확인 항목: 누락된 의존성, 설정 오류, 훅 설치 상태, 에이전트 가용성

---

## 사용법

### 방법 1: 매직 키워드 (가장 간단)

프롬프트 아무 곳에나 키워드를 포함하면 자동 활성화된다:

```bash
# 완전 자동 실행
autopilot: REST API 만들어줘

# 최대 병렬 처리
ulw 모든 에러 수정해줘

# 지속 실행 (완료될 때까지 포기 안 함)
ralph: 인증 시스템 리팩토링해줘

# 토큰 절약
eco: 데이터베이스 마이그레이션 해줘

# 계획 수립
plan this feature

# TDD 방식
tdd: 비밀번호 검증 구현해줘

# 에이전트 군단
swarm 5 agents: 모든 린트 에러 수정해줘
```

**전체 매직 키워드 목록:**

| 키워드 | 활성화 모드 |
|--------|------------|
| `autopilot` / `build me` | 완전 자동 실행 |
| `ultrawork` / `ulw` / `uw` | 병렬 에이전트 오케스트레이션 |
| `ultrapilot` / `parallel build` | 병렬 오토파일럿 (3-5배) |
| `ralph` / `don't stop` / `must complete` | 지속 실행 |
| `eco` / `ecomode` / `save-tokens` | 토큰 효율 |
| `plan this` / `plan the` | 계획 인터뷰 |
| `search` / `find` / `locate` | 향상된 검색 |
| `analyze` / `investigate` / `debug` | 깊이있는 분석 |
| `research` / `analyze data` | 병렬 연구 |
| `tdd` / `test first` | TDD 워크플로우 |

### 방법 2: 슬래시 커맨드

```bash
# 실행 모드
/oh-my-claudecode:autopilot <task>         # 완전 자동 실행
/oh-my-claudecode:ultrawork <task>         # 최대 병렬 처리
/oh-my-claudecode:ultrapilot <task>        # 병렬 오토파일럿
/oh-my-claudecode:ralph <task>             # 지속 실행
/oh-my-claudecode:ecomode <task>           # 토큰 절약
/oh-my-claudecode:swarm <N>:<agent>        # 에이전트 군단
/oh-my-claudecode:pipeline <stages>        # 순차 파이프라인

# 코드 품질
/oh-my-claudecode:code-review              # 코드 리뷰
/oh-my-claudecode:security-review          # 보안 검토
/oh-my-claudecode:tdd <feature>            # TDD 워크플로우
/oh-my-claudecode:ultraqa <goal>           # 자동 QA 순환
/oh-my-claudecode:build-fix                # 빌드 오류 수정

# 분석/탐색
/oh-my-claudecode:analyze <target>         # 깊이있는 분석
/oh-my-claudecode:deepsearch <query>       # 철저한 코드 검색
/oh-my-claudecode:deepinit [path]          # 코드베이스 인덱싱 (AGENTS.md 생성)
/oh-my-claudecode:research <topic>         # 병렬 연구

# 계획
/oh-my-claudecode:plan <description>       # 계획 세션
/oh-my-claudecode:ralplan <description>    # 반복 계획 합의
/oh-my-claudecode:ralph-init <task>        # PRD 초기화

# 유틸리티
/oh-my-claudecode:orchestrate <task>       # 멀티에이전트 활성화
/oh-my-claudecode:note <content>           # 메모 저장
/oh-my-claudecode:learner                  # 스킬 추출/학습
/oh-my-claudecode:cancel                   # 현재 모드 취소
/oh-my-claudecode:help                     # 도움말
/oh-my-claudecode:hud                      # HUD 설정
/oh-my-claudecode:doctor                   # 진단/트러블슈팅
/oh-my-claudecode:omc-setup                # 초기 설정
```

### 방법 3: CLI 명령어 (터미널)

```bash
omc wait              # 레이트 제한 상태 확인
omc wait --start      # 자동 재개 활성화
omc wait --stop       # 자동 재개 비활성화
omc stats             # 현재 세션 통계
omc cost daily        # 일일 비용 보고
omc cost weekly       # 주간 비용 보고
omc agents            # 에이전트 분석
```

---

## 실전 활용 시나리오

### 시나리오 1: 새 기능 빠르게 구현

```bash
autopilot: Spring Boot에 회원가입 API 추가해줘
```
→ 에이전트가 자동으로 Entity, Repository, Service, Controller 생성

### 시나리오 2: 대규모 리팩토링

```bash
ralph: 모든 컨트롤러에 유효성 검사 추가해줘
```
→ 완료될 때까지 반복 실행, 중간에 에러나도 계속 진행

### 시나리오 3: 토큰 아끼면서 작업

```bash
eco: 테스트 코드 작성해줘
```
→ 30-50% 토큰 절감하면서 실행

### 시나리오 4: 병렬로 여러 작업 동시 처리

```bash
ulw 모든 TODO 주석 찾아서 구현해줘
```
→ 여러 에이전트가 동시에 다른 파일 작업

### 시나리오 5: 코드 품질 점검

```bash
/oh-my-claudecode:code-review
/oh-my-claudecode:security-review
```
→ 코드 리뷰 + 보안 취약점 자동 분석

### 시나리오 6: 코드베이스 파악

```bash
/oh-my-claudecode:deepinit
```
→ 프로젝트 구조를 분석하여 AGENTS.md 문서 자동 생성

---

## HUD 설정

`~/.claude/settings.json`에서 상태 표시줄을 커스터마이징할 수 있다:

```json
{
  "omcHud": {
    "preset": "focused",
    "elements": {
      "cwd": true,
      "gitRepo": true,
      "gitBranch": true,
      "contextBar": true,
      "agents": true,
      "todos": true,
      "ralph": true,
      "autopilot": true
    }
  }
}
```

**프리셋 종류:** `minimal`, `focused`, `full`, `dense`, `analytics`, `opencode`

---

## 트러블슈팅

| 문제 | 해결책 |
|------|--------|
| 명령어를 찾을 수 없음 | `/oh-my-claudecode:omc-setup` 재실행 |
| 훅이 실행되지 않음 | 권한 확인: `chmod +x ~/.claude/hooks/**/*.sh` |
| 에이전트가 위임되지 않음 | CLAUDE.md 로드 확인 |
| 토큰 한계 오류 | `/oh-my-claudecode:ecomode` 사용 |
| 캐시 문제 | `/oh-my-claudecode:doctor`로 캐시 정리 |

---

## 7가지 실행 모드

| 모드 | 키워드 | 설명 | 특징 |
|------|--------|------|------|
| **Autopilot** | `autopilot` | 완전 자동 워크플로우 | 빠른 자율 실행 |
| **Ultrawork** | `ulw` | 최대 병렬 처리 | 동시 다발적 작업 |
| **Ralph** | `ralph` | 지속적 실행 모드 | 완료될 때까지 포기하지 않음 |
| **Ultrapilot** | `ultrapilot` | 다중 컴포넌트 처리 | 3-5배 빠른 속도 |
| **Ecomode** | `eco` | 토큰 절감 모드 | 30-50% 비용 절감 |
| **Swarm** | `swarm` | 조정된 병렬 작업 | 독립 태스크 병렬 실행 |
| **Pipeline** | `pipeline` | 순차적 다단계 처리 | 스테이지별 실행 |

### 사용 예시

```
autopilot: build a REST API for managing tasks
ralph: refactor auth system
ulw fix all errors in the project
eco: migrate database schema
plan the API architecture
```

> Ralph 모드는 Ultrawork의 병렬 기능을 자동 포함하므로 별도 조합 불필요.

---

## 32개 전문 에이전트

### 기본 에이전트

| 에이전트 | 역할 |
|----------|------|
| `architect` | 아키텍처 설계 |
| `analyst` | 분석 |
| `build-fixer` | 빌드 오류 수정 |
| `code-reviewer` | 코드 리뷰 |
| `critic` | 비판적 검토 |
| `designer` | UI/UX 설계 |
| `executor` | 실행 |
| `deep-executor` | 심층 실행 |
| `explore` | 코드베이스 탐색 |
| `git-master` | Git 작업 |
| `planner` | 계획 수립 |
| `qa-tester` | QA 테스트 |
| `researcher` | 조사/리서치 |
| `scientist` | 데이터 과학 |
| `security-reviewer` | 보안 리뷰 |
| `tdd-guide` | TDD 가이드 |
| `vision` | 비전/이미지 |
| `writer` | 문서 작성 |

### 난이도별 변형 (Tiered Agents)

작업 복잡도에 따라 에이전트가 자동 분기된다.

| 난이도 | 변형 에이전트 |
|--------|---------------|
| **Low** | architect, build-fixer, code-reviewer, designer, explore, qa-tester, researcher, scientist, security-reviewer, tdd-guide |
| **Medium** | architect, explore |
| **High** | designer, executor, explore, qa-tester, scientist |

### 스마트 모델 라우팅

- **간단한 작업** → Haiku (빠르고 저렴)
- **복잡한 추론** → Opus (정확하고 깊은 분석)
- 자동 위임 시스템으로 적절한 에이전트에 작업 배분

---

## 37개 스킬 (Skills)

| 카테고리 | 스킬 |
|----------|------|
| **실행 모드** | autopilot, ultrawork, ultrapilot, ecomode, ralph, ralph-init, ralplan, swarm, pipeline |
| **코드 품질** | code-review, security-review, tdd, ultraqa, review |
| **분석/탐색** | analyze, deepsearch, deepinit, research, explore (내장) |
| **개발 도구** | build-fix, git-master, frontend-ui-ux, deep-executor |
| **프로젝트 관리** | plan, orchestrate, project-session-manager, note |
| **문서/학습** | writer-memory, learner, learn-about-omc, help, skill |
| **시스템** | omc-setup, doctor, cancel, hud, trace, mcp-setup, local-skills-setup, release |

---

## 프로젝트 구조

```
oh-my-claudecode/
├── agents/                    # 32개 전문 에이전트 정의 (Markdown)
│   ├── templates/             # 에이전트 템플릿
│   ├── architect.md           # 아키텍처 에이전트
│   ├── architect-low.md       # 난이도별 변형
│   ├── executor.md
│   └── ...
├── skills/                    # 37개 재사용 가능 스킬
│   ├── autopilot/
│   ├── ralph/
│   ├── ultrawork/
│   ├── ecomode/
│   └── ...
├── src/                       # 핵심 소스 코드 (TypeScript)
│   ├── agents/                # 에이전트 로직
│   ├── analytics/             # 분석 시스템
│   ├── cli/                   # CLI 인터페이스
│   ├── commands/              # 커맨드 정의
│   ├── compatibility/         # 호환성 처리
│   ├── config/                # 설정
│   ├── features/              # 기능 구현
│   ├── hooks/                 # 훅 시스템
│   ├── hud/                   # HUD 상태 표시줄
│   ├── installer/             # 설치 모듈
│   ├── lib/                   # 라이브러리
│   ├── mcp/                   # MCP 연동
│   ├── platform/              # 플랫폼 지원
│   ├── shared/                # 공유 코드
│   ├── tools/                 # 유틸리티 도구
│   ├── utils/                 # 유틸 함수
│   ├── verification/          # 검증 모듈
│   ├── __tests__/             # 테스트
│   └── index.ts               # 진입점
├── commands/                  # CLI 명령어
├── docs/                      # 문서
│   ├── ARCHITECTURE.md
│   ├── REFERENCE.md
│   ├── FEATURES.md
│   ├── ANALYTICS-SYSTEM.md
│   ├── PERFORMANCE-MONITORING.md
│   ├── TIERED_AGENTS_V2.md
│   ├── DELEGATION-ENFORCER.md
│   ├── COMPATIBILITY.md
│   ├── MIGRATION.md
│   ├── SYNC-SYSTEM.md
│   └── ...
├── examples/                  # 사용 예제
├── tests/                     # 테스트 스위트
└── dist/                      # 배포 파일
```

---

## 주요 기능 상세

### 1. HUD (Heads-Up Display)
- 실시간 상태 표시줄
- 현재 실행 중인 에이전트, 진행 상황 시각화

### 2. 스킬 추출 (Learner)
- 문제 해결 패턴을 자동 학습
- 반복 작업을 스킬로 저장하여 재사용

### 3. 자동 위임 (Delegation Enforcer)
- 작업 복잡도 분석 후 적절한 에이전트에 자동 배분
- 난이도별 에이전트 변형 자동 선택 (Low/Medium/High)

### 4. 분석 시스템 (Analytics)
- 토큰 사용량, 실행 시간, 성공률 추적
- 성능 모니터링 및 최적화

### 5. MCP 연동
- Model Context Protocol 서버 설정 지원
- 외부 도구와의 통합

---

## 핵심 가치

| 항목 | 설명 |
|------|------|
| 설정 불필요 | 기본값으로 즉시 작동 |
| 비용 최적화 | Ecomode로 30-50% 토큰 절감 |
| 실시간 시각화 | HUD 상태 표시줄 |
| 패턴 학습 | 문제 해결 방식 자동 추출/재사용 |
| 지속적 실행 | Ralph 모드 - 완료될 때까지 포기하지 않음 |
| 병렬 처리 | Ultrawork/Swarm으로 동시 다발적 작업 |
