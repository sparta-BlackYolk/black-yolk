# 1단계: 빌드 이미지
FROM openjdk:17-jdk-slim AS builder

# 패키지 설치 및 Gradle 설치
RUN apt-get update && \
    apt-get install -y gradle && \
    apt-get clean

# 작업 디렉토리 설정
WORKDIR /app

# 프로젝트 파일 복사
COPY . .

# gradlew에 실행 권한 부여
RUN chmod +x gradlew

# Gradle 빌드 실행 및 결과 확인
RUN ./gradlew clean build -x test && ls build/libs

# 2단계: 실행 이미지
FROM openjdk:17-slim AS final

# 빌드 단계에서 생성된 JAR 파일 복사
COPY --from=builder /app/build/libs/delivery-service-0.0.1-SNAPSHOT.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]