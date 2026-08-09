# Raffiné - Backend

## 프로젝트 소개

Raffiné 서비스의 프론트엔드 레포지토리입니다. 스스로 무엇을 좋아하는지 모르는 사람들을 위해 사용자가 자신의 취향과 경험을 게시글로 기록하고, 다른 사용자와 댓글 및 토론을 통해 생각을 나누는 웹 서비스 입니다.

### 서비스 사용
- [Raffiné](http://54.180.97.37/)

## 기능 및 목적

- 사용자 회원가입, 로그인, 로그아웃, 계정 관리 API 제공
- 게시글 목록 조회, 상세조회, 작성, 수정, 삭제 API 제공
- 게시글 이미지 및 사용자 프로필 이미지 저장 기능 제공
- 댓글 작성 및 댓글 목록 조회 API 제공
- 토론 메시지 저장 및 조회 기능 제공
- WebSocket 기반 토론 메시지 통신 지원
- Spring Security 기반 인증 및 CSRF 보호 적용
- 프론트엔드와 연동되는 서버 사이드 비즈니스 로직 구현

## 기술 스택

| 구분 | 기술 | 사용 목적 |
| --- | --- | --- |
| Language | Java | 백엔드 애플리케이션 로직 작성 |
| Framework | Spring Boot | 서버 애플리케이션 구성 및 실행 |
| Web | Spring Web MVC | REST API 요청 및 응답 처리 |
| Persistence | Spring Data JPA | 데이터베이스 연동 및 엔티티 관리 |
| Security | Spring Security | 로그인, 인증, 권한, CSRF 처리 |
| Validation | Spring Validation | 요청 데이터 검증 |
| Realtime | Spring WebSocket | 토론 메시지 실시간 통신 지원 |
| Database | H2 Database | 로컬 및 개발 환경 데이터 저장 |
| Boilerplate | Lombok | 반복 코드 감소 |
| Build Tool | Gradle | 빌드, 테스트, 의존성 관리 |
| Container | Docker | 컨테이너 기반 실행 환경 구성 |

### Frontend
- [KTB4_Gianna_FE](https://github.com/100-hours-a-week/KTB4_Gianna_FE)

### 시연 영상
https://drive.google.com/drive/folders/1nY8cvNMZ4tuO2aPLpwDmIP00zTnJiwid?usp=sharing

## 설치 방법

### 요구 사항

- Java
- Gradle 또는 프로젝트에 포함된 Gradle Wrapper

### 설치 및 빌드

```bash
./gradlew clean build
```

### 서버 실행

```bash
./gradlew bootRun
```

서버는 기본적으로 `8080` 포트에서 실행됩니다.

### 테스트 실행

```bash
./gradlew test
```

### Docker 빌드 및 실행

```bash
docker build -t raffine-backend .
docker run -p 8080:8080 raffine-backend
```

## 문제 해결 방법

### 프론트엔드에서 API 요청이 실패하는 경우

백엔드 서버가 `8080` 포트에서 실행 중인지 확인합니다. 프론트엔드 개발 서버는 백엔드 API 요청을 `localhost:8080`으로 프록시하도록 설정되어 있습니다.

### 이미지 업로드가 실패하는 경우

업로드 파일 크기가 설정된 제한을 넘지 않는지 확인합니다. 현재 multipart 설정은 파일 1개 최대 5MB, 요청 전체 최대 6MB입니다.

### 서버 실행 중 포트 충돌이 발생하는 경우

이미 `8080` 포트를 사용 중인 프로세스가 있는지 확인합니다. 필요하다면 실행 중인 서버를 종료하거나 설정 파일에서 포트를 변경합니다.

## 지원 창구

프로젝트 관련 문의, 버그 제보, 기능 제안은 아래 메일 주소로 연락 부탁드리겠습니다.

Gmail : yeobinlee1210@gmail.com
