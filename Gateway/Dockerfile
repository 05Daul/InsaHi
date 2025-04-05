#  Gradle 빌드 스테이지
FROM gradle:8.11.1-jdk17 AS build

# 작업 디렉토리 설정
WORKDIR /myapp

# 전체 프로젝트 복사
COPY . /myapp

# 실행 권한 부여
RUN chmod +x gradlew

# 테스트 생략하고 빌드 (더 빠르게)
RUN ./gradlew :Gateway:clean :Gateway:bootJar --no-daemon -x test

# 실행 환경 (경량 Alpine)
FROM openjdk:17-alpine

# 작업 디렉토리 설정
WORKDIR /myapp

# 빌드 결과 JAR 파일 복사
COPY --from=build /myapp/Gateway/build/libs/*.jar /myapp/gateway-service.jar

# 포트
EXPOSE 1006

# Timezone 설정 (optional)
ENV TZ=Asia/Seoul

# 환경변수 (실제 값은 외부에서 주입)
ENV SPRING_PROFILES_ACTIVE=prod

# 실행 명령
ENTRYPOINT ["java", "-jar", "/myapp/gateway-service.jar"]
