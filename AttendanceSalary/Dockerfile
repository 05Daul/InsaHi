# Gradle 빌드 스테이지
FROM gradle:8.11.1-jdk17 AS build

# 작업 디렉토리 설정
WORKDIR /myapp

# 전체 프로젝트 복사
COPY . /myapp

# 실행 권한 부여
RUN chmod +x gradlew

# 특정 모듈(AttendanceSalary)만 빌드하도록 수정
RUN ./gradlew :AttendanceSalary:clean :AttendanceSalary:bootJar --no-daemon -x test

# 실행 환경 (경량 Alpine)
FROM openjdk:17-alpine

# 작업 디렉토리 설정
WORKDIR /myapp

# 빌드 결과 JAR 파일 복사
COPY --from=build /myapp/AttendanceSalary/build/libs/*.jar /myapp/atdsal.jar

# 포트
EXPOSE 1000

# 실행 명령
ENTRYPOINT ["java", "-jar", "/myapp/atdsal.jar"]