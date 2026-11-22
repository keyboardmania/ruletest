# ruletest

프론트엔드에서만 동작하는 할 일 목록입니다.

## 주요 기능

- 할 일 등록
- 할 일 목록 조회
- 완료 상태 변경
- 할 일 삭제
- 빈 입력 방지
- 완료한 항목과 미완료 항목의 시각적 구분

할 일은 화면의 React 상태로만 유지됩니다. 새로고침하면 목록은 초기화됩니다. 백엔드 API, 데이터베이스, 인증은 사용하지 않습니다.

## 기술 스택

- Next.js App Router
- React
- TypeScript

## 디렉터리 구조

- `app`: App Router 화면
- `components`: 할 일 목록 UI
- `.cursor/rules`: 팀 Cursor Rules

## 실행 방법

환경변수는 사용하지 않습니다.

```bash
npm install
npm run dev
```

개발 서버 주소는 http://localhost:3000 입니다.

## 협업

브랜치는 `main`, `develop`, `feature`를 사용합니다. 작업 브랜치 이름은 `타입/이슈번호-작업내용`입니다.
