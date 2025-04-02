#  Gradle 빌드 스테이지
FROM gradle:8.11.1-jdk17 AS build

# 작업 디렉토리 설정
WORKDIR /myapp

# 전체 프로젝트 복사
COPY . /myapp

# 실행 권한 부여
RUN chmod +x gradlew

# 테스트 생략하고 빌드 (더 빠르게)
RUN ./gradlew :ElectronicApproval:clean :ElectronicApproval:bootJar --no-daemon -x test

# 실행 환경 (경량 Alpine)
FROM openjdk:17-alpine

# 작업 디렉토리 설정
WORKDIR /myapp

# 빌드 결과 JAR 파일 복사
COPY --from=build /myapp/ElectronicApproval/build/libs/*.jar /myapp/electronicapproval.jar

# 포트
EXPOSE 1005
ENV SPRING_PROFILES_ACTIVE=prod

# 실행 명령
ENTRYPOINT ["java", "-jar", "/myapp/electronicapproval.jar"]
