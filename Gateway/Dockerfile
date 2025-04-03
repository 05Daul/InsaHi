FROM gradle:8.11.1-jdk17 AS build

WORKDIR /myapp

#전체 프로젝트 복사
COPY . /myapp

#실행 권한 부여
RUN chmod +x gradlew

#테스트 생략하고 빌드
RUN ./gradlew clean build --no-daemon -x test

#---------------------------
#2단계: 실제 실행용 이미지 (경량)
#---------------------------
FROM openjdk:17-alpine

#작업 디렉토리 설정
WORKDIR /myapp

#빌드 결과 복사
COPY --from=build /myapp/build/libs/*.jar /myapp/gateway-service.jar
COPY --from=build /myapp/src/main/resources/application-prod.yml /myapp/config/application-prod.yml

#포트 오픈
EXPOSE 1006

#Timezone 설정 (선택)
ENV TZ=Asia/Seoul

#Spring profile 및 설정 위치 명확히 지정
ENTRYPOINT ["java", "-jar", "/myapp/gateway-service.jar", "--spring.config.additional-location=file:/myapp/config/", "--spring.profiles.active=prod"]