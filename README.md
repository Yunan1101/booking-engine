# Booking Engine Project

본 프로젝트는 예약 시스템(Booking Engine)을 구축하기 위한 백엔드 애플리케이션입니다. 

## 🛠 기술 스택 및 개발 환경
- **운영체제:** WSL2 (Ubuntu)
- **언어:** Java 21 (LTS)
- **프레임워크:** Spring Boot 3.4
- **데이터베이스:** MySQL 8.4 (Docker Engine CLI)

## 핵심 개발 원칙 (Architecture Guidelines)
본 프로젝트는 다음의 원칙을 철저히 준수하며 개발됩니다.
1. **클린 코드 & 객체지향:** SOLID 원칙에 입각한 유지보수가 용이한 코드 작성.
2. **동시성 제어:** 예약 도메인의 특성을 고려한 락(Lock) 전략(낙관적/비관적/분산 락) 구현.
3. **DB 최적화:** MySQL 8.4 환경에 최적화된 스키마 설계 및 인덱스 전략 수립.
4. **테스트 주도:** JUnit 5 기반의 단위 테스트 및 통합 테스트 작성.

## 인프라 구성 (Docker Compose)
로컬 데이터베이스 테스트를 위한 환경이 구성되어 있습니다. (`compose.yaml`)
- **MySQL 8.4:** 포트 `3306` (데이터 영속성을 위한 볼륨 마운트 적용)
- **phpMyAdmin:** 포트 `8080` (브라우저 기반 DB 관리 툴)

## 진행 상황
- [x] 프로젝트 협업 원칙 및 페르소나 정의 (`.antigravity/instructions.md`)
- [x] 로컬 데이터베이스 Docker Compose 설정
- [x] Git 초기화 및 GitHub 원격 저장소 연동 (`main` 브랜치)
- [ ] Spring Boot 3.4 프로젝트 스캐폴딩 및 HikariCP 최적화
- [ ] 핵심 도메인 설계 및 Entity 작성
