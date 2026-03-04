# GitHub Copilot Instructions

## 일반 원칙
- 코드 리뷰 및 응답은 **한국어**로 작성
- Kotlin 공식 스타일 및 안드로이드 공식 스타일 준수
- `return`이 `Unit`인 경우 반환 타입 선언 생략
- 보안 민감 정보(토큰, 비밀번호 등) 노출 여부 확인

---

## Clean Architecture 레이어별 규칙

### `domain/**`
1. 순수 Kotlin/Java만 허용 (`android.*`, `androidx.*` 등 금지)
2. `data`, `feature`, `koin`, `core` 모듈 의존 금지
3. `UseCase`는 단일 책임 원칙 준수 (`execute` / `invoke` 메서드 하나)
4. `Repository`는 `interface`만 정의 (구현체 금지)

### `data/**`
1. `domain` 이외 모듈 의존 금지
2. API 엔드포인트에 `@Header`로 인증을 별도 추가하지 않음 (retrofit 빌드 시 주입)
3. `DataSource`, `Repository`, `RepositoryImpl`에 `@Singleton` 어노테이션 불필요

### `koin/**`
1. XML 컨벤션 준수
2. UI 관련 문자열은 반드시 `strings.xml`로 분리
3. `ViewModel`에서 `Context` 사용 금지
4. `Repository` 직접 접근 금지

### `feature/**`
1. XML 컨벤션 준수
2. UI 관련 문자열은 반드시 `strings.xml`로 분리
3. `ViewModel`에서 `Context` 사용 금지
4. `data` 모듈 의존 금지, `Repository` 직접 접근 금지
5. Compose 최적화를 위해 State 클래스에 `@Immutable` 및 `ImmutableList` 사용 필수

### `core/designsystem/**`
1. 기존 컴포넌트 수정 시 하위 호환성 반드시 유지
2. 컴포넌트 설계 시 확장 가능성 고려

---

## 피처별 특수 규칙

### `feature/article/**`
- `ArticleBoardType` ID는 백엔드 계약값으로 **절대 변경 금지**
  - `ALL=4`, `NORMAL=5`, `SCHOLARSHIP=6`, `SCHOOL=7`, `RECRUIT=8`, `KOIN=9`, `IPP=12`, `STUDENT=13`, `LOSTANDFOUND=14`
  - 비연속 값이므로 순서 가정 금지

### `feature/banner/**`
- 배너 버전 비교 방향 고정: `banner.version > currentKoinVersion` 일 때만 외부 이동 (부등호 방향 변경 금지)

### `feature/bus/**`
- 버스 모듈은 `UseCase` 레이어 없이 `BusRepository` 직접 호출

### `feature/chat/**`
- WebSocket 해제(`disconnectWS`)는 반드시 별도 `CoroutineScope`에서 실행
- `viewModelScope`로 변경 시 화면 종료 후 연결 해제 안 됨

### `feature/club/**`
- `postClubQnaUseCase`의 `parentId`: 새 질문이면 `null`, 답글이면 non-null (의미 변경 금지)

### `feature/dining/**`
- 날짜 API 파라미터 형식은 `YYMMDD(String)` 고정 (다른 형식 변경 시 API 오류 발생)
- 비운영 필터링 문자열은 정확히 `"미운영"`

### `feature/user/**`
- 비밀번호 해싱은 `UserLoginUseCase` 내부에서 처리 (ViewModel에서 직접 해싱 후 전달 시 이중 해싱 발생)
- 토큰 저장은 `UserLoginUseCase` 내부에서 처리 (ViewModel에서 직접 저장 금지)
