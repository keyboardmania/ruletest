# ruletest

Spring Boot로 동작하는 할 일 API입니다.

## 주요 기능

- 할 일 생성
- 할 일 목록 조회
- 할 일 단건 조회
- 할 일 전체 수정
- 할 일 삭제
- 빈 제목과 필수값 검증

인증과 프론트엔드 연동은 포함하지 않습니다. 개발과 테스트는 인메모리 H2를 사용합니다.

## 기술 스택

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Bean Validation
- H2

## 디렉터리 구조

- `backend`: Todo API
- `.cursor/rules`: 팀 Cursor Rules

## 실행 방법

환경변수 `DB_USERNAME`, `DB_PASSWORD`가 없으면 애플리케이션이 시작하지 않습니다. 운영에서는 `DB_URL`도 필요합니다. 실제 Secret은 `.env.example`에 적지 않습니다.

```bash
cd backend
./mvnw test
```

개발 프로필:

```bash
cd backend
# DB_USERNAME, DB_PASSWORD, SPRING_PROFILES_ACTIVE=dev
./mvnw spring-boot:run
```

## API

기본 경로는 `/api/v1/todos`입니다. 식별자는 경로 대신 요청 본문으로 전달합니다.

성공 응답 필드는 `success`, `respondedAt`, `code`, `message`, `data`입니다. 성공 시 `code`는 HTTP 상태 번호입니다. 삭제 성공은 본문 없는 `204`입니다.

검증 오류는 HTTP `400`, `code` `4000`, `message` `입력값이 올바르지 않다.`이고 `data`에 필드와 메시지를 담습니다. 없는 할 일은 HTTP `404`, `code` `4400`입니다. `4400`은 `400`대 코드 `4000~4099`와 겹치지 않는 `404` 범위입니다.

| 메서드 | 경로 | 본문 | 성공 |
| --- | --- | --- | --- |
| POST | `/api/v1/todos` | `title` | 201 |
| GET | `/api/v1/todos` | 없음 | 200, `data`는 배열 |
| GET | `/api/v1/todos/detail` | `todoId` | 200 |
| PUT | `/api/v1/todos` | `todoId`, `title`, `completed` | 200 |
| DELETE | `/api/v1/todos` | `todoId` | 204 |

테이블은 `todo`이고 컬럼은 `todo_id`, `title`, `completed`, `created_at`입니다.

## 협업

브랜치는 `main`, `develop`, `feature`를 사용합니다. 작업 브랜치 이름은 `타입/이슈번호-작업내용`입니다.
