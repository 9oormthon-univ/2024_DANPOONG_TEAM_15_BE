# Ivory-Backend

9oormthonUNIV 단풍톤 프로젝트의 백엔드 서버입니다.

---

## 🚀 시작하기 (Getting Started)

### 📋 요구사항 (Prerequisites)
- **Java 17 이상**
- **Spring Boot 3.0 이상**
- **Maven 3.8+** 또는 **Gradle 7.6+**
- **MySQL 8.0 이상**
- **Docker** (옵션)

---

### 📁 프로젝트 설정 (Setup)

1. **코드 복제**
   ```bash
   git clone https://github.com/9oormthon-univ/2024_DANPOONG_TEAM_15_BE.git
   cd 2024_DANPOONG_TEAM_15_BE
    ```
2. 환경 변수 설정
   프로젝트 루트 디렉토리에 .env 파일을 생성하고 아래 내용을 추가하세요:
    ```bash
    DB_URL=
    MYSQL_USER=
    MYSQL_PASSWORD=
    MYSQL_ROOT_PASSWORD=
    MYSQL_DATABASE=
    
    JWT_SECRET_KEY=
    ```
---
### 🐳 Docker로 개발 환경 실행하기 (Using Docker)
   ```bash
   docker-compose -f docker-compose.dev.yml up --build -d
   ```
---
### 🔍 Swagger로 API 문서 확인하기
   - [http://localhost:8080/swagger-ui/index.html#](http://localhost:8080/swagger-ui/index.html#)
---