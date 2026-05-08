# Concert Ticketing System (Booking Engine)

<div align="center">
  <img src="https://img.shields.io/badge/Java%2021-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white">
  <img src="https://img.shields.io/badge/Spring%20Boot%203.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/MySQL%208.4-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
</div>

<br/>

대규모 트래픽이 발생하는 **콘서트 티켓팅 상황**을 가정하고, **동시성 문제(Concurrency Issue)**를 안전하고 완벽하게 제어하기 위해 구축한 백엔드 예약 시스템입니다. 사용자가 동시에 동일한 좌석을 예약하려고 할 때 발생할 수 있는 초과 예매(Overbooking)를 방지하는 데 초점을 맞추었습니다.

## 핵심 기능 및 특징

- **완벽한 동시성 제어 (Pessimistic Lock)**
  - 다수의 사용자가 동시에 동일한 좌석을 요청하더라도 DB 락(X-Lock)을 통해 **단 1명만 예매 성공**하도록 보장.
  - 낙관적 락(Optimistic Lock) 대비 충돌이 100% 예상되는 티켓팅 환경에 최적화된 비관적 락(Pessimistic Lock) 도입.
- **조회 성능 최적화**
  - 특정 회차의 예약 가능 좌석 조회를 위해 `Seat` 테이블에 복합 인덱스(Composite Index) 적용.
- **자동화된 테스트 및 검증**
  - `CountDownLatch`와 `ExecutorService`를 활용한 멀티스레드 환경의 통합 테스트(JUnit5) 구축.
  - Shell Script를 활용한 N명 규모의 동시 접속 부하 테스트 스크립트 제공 (`concurrency_test.sh`).
- **API 문서 자동화 (Swagger UI)**
  - Springdoc OpenAPI를 활용하여 코드가 곧 문서가 되도록 구현(Docs as Code).
- **시각화 프론트엔드 UI**
  - 동시성 예매 시각화를 위한 직관적인 다크 모드(Dark Mode) 및 글래스모피즘(Glassmorphism) UI 제공.

---

## 기술 스택

### Backend
- **Language**: Java 21 (LTS)
- **Framework**: Spring Boot 3.4
- **JPA & ORM**: Spring Data JPA, Hibernate

### Database & Infrastructure
- **Database**: MySQL 8.4
- **Connection Pool**: HikariCP (maximum-pool-size 최적화)
- **Infra**: Docker Compose

### Testing & Documentation
- **Test**: JUnit 5
- **API Docs**: Swagger UI (springdoc-openapi)

---

## 시작하기 (Getting Started)

프로젝트를 로컬 환경에서 실행하는 방법입니다.

### 1. 요구 사항
- Docker 및 Docker Compose
- Java 21
- Gradle

### 2. 데이터베이스 구동
Docker Compose를 사용하여 MySQL 8.4 및 phpMyAdmin을 백그라운드에서 실행합니다.
```bash
docker compose up -d
```
> MySQL은 `3306` 포트, phpMyAdmin은 `8080` 포트로 실행됩니다.

### 3. 애플리케이션 실행
Gradle 래퍼를 사용하여 Spring Boot 서버를 실행합니다.
```bash
./gradlew bootRun
```
> 서버 구동 시 초기 테스트를 위한 기초 데이터(유저, 콘서트, 좌석 정보 등)가 자동 주입됩니다.

### 4. API 문서 및 시스템 접속
- **Swagger UI (API 명세서):** [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
- **티켓팅 시각화 웹 화면:** [http://localhost:8081/](http://localhost:8081/)

---

## 테스트 및 동시성 검증

### 자동화된 통합 테스트
멀티스레드 환경에서의 동시성 제어가 완벽히 작동하는지 통합 테스트로 자동 검증합니다.
```bash
./gradlew test
```

### 쉘 스크립트 기반 부하 테스트
실제 터미널 환경에서 백그라운드 프로세스를 생성하여 동시에 여러 예약 요청을 쏘는 부하 테스트를 진행합니다. 초과 예매가 방지되고 단 1건만 예약되는지 눈으로 확인할 수 있습니다.
```bash
# 실행 권한 부여
chmod +x concurrency_test.sh

# 동시 접속 예매 테스트 실행
./concurrency_test.sh
```

---

## 기술적 의사결정 및 고민 (Architecture Notes)

개발 과정에서의 트러블슈팅, 동시성 제어 방식에 대한 고민, 그리고 왜 이러한 기술을 선택했는지에 대한 깊이 있는 기록은 아래 아키텍트 노트에 정리되어 있습니다.

**[아키텍트 노트 (ARCHITECT_NOTES.md) 보러가기](./ARCHITECT_NOTES.md)**
