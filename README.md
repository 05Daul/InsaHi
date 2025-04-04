## 📝 개요. 서비스 기획서

<details>
<summary>🚀 1. 요구사항 명세서</summary>

- [GoogleSheets 링크 또는 내용](https://docs.google.com/spreadsheets/d/1C_o3BqHqdRivUo3Bq-F3Qa8QamxaHs0BxE9J6SA4qGc/edit?gid=1900060456#gid=1900060456)

</details>

<details>
<summary>🚀 2. 테스트 계획서</summary>

- [GoogleSheets 링크 또는 내용](https://docs.google.com/spreadsheets/d/1C_o3BqHqdRivUo3Bq-F3Qa8QamxaHs0BxE9J6SA4qGc/edit?gid=1796381502#gid=1796381502)

</details>

<details>
<summary>🚀 3. ERD (Entity Relationship Diagram)</summary>

<img width="1100" alt="ERD" src="https://github.com/user-attachments/assets/2d63d405-3822-4f46-8702-c723f939e422" />

</details>

<details>
<summary>🚀 4. WBS (Work Breakdown Structure)</summary>

<img width="1100" alt="WBS" src="https://github.com/user-attachments/assets/5914a2a1-cbf6-4338-8138-42e6d45c8cf7" />

</details>

<details>
<summary>🚀 5. 화면 정의서</summary>

- **회사 회원가입 페이지**  
![회원가입페이지](https://github.com/user-attachments/assets/9d13c514-bb96-4648-b9b5-fc3bf923d0a0)

- **로그인페이지**
![로그인페이지](https://github.com/user-attachments/assets/7285b4bc-8006-4170-8e67-e23309dcbeec)**

- **마이페이지**
![마이페이지](https://github.com/user-attachments/assets/1f981aa5-9ee6-433d-9276-6be25653164f)

- **개인정보수정페이지**
![개인정보수정페이지](https://github.com/user-attachments/assets/58de7cba-f9a1-4c39-9d89-ed48042a1494)


- **비밀번호 변경 페이지**
![비밀번호변경페이지](https://github.com/user-attachments/assets/8e2d3ea2-f7f4-48b3-a0c7-f03db4438040)


- **직원등록페이지**
![직원등록페이지](https://github.com/user-attachments/assets/4dde8132-3972-43ab-98c2-39cce01e8a8d)

  

- **관리자 설정 페이지**  
  ![004](https://github.com/user-attachments/assets/13e9d16e-b32b-4ac1-b220-a2a5eaf251ee)

- **급여 페이지**  
  ![003](https://github.com/user-attachments/assets/5cb9a755-4573-42a0-b959-0d214ec60f8d)

- **근태 페이지**  
  ![002](https://github.com/user-attachments/assets/156d6456-9e93-44f3-a56e-ec0f5b52538b)

- **휴가 페이지**  
  1) 휴가 신청  
     ![006](https://github.com/user-attachments/assets/265f8754-eb45-4320-b379-c13a621917ba)  
  2) 휴가 신청 내역 관리  
     ![007](https://github.com/user-attachments/assets/115b5178-7c98-4f90-9e6c-884d2ffe8add)  
  3) 휴가 신청 상세 보기  
     ![008](https://github.com/user-attachments/assets/dc015dc2-d5aa-43e8-beba-068b660de0fb)

- **결재 페이지**  
  1) 결재 문서 상신  
     ![009](https://github.com/user-attachments/assets/a6440241-6dab-413d-a19e-0443fc21426d)  
  2) 기안서  
     ![010](https://github.com/user-attachments/assets/db206275-0342-4d5e-ad45-2e674ac94c72)  
  3) 결재 문서 목록  
     ![011](https://github.com/user-attachments/assets/d11a6920-be13-4d5d-a8db-c8ae8ce2b706)

- **주소록 페이지**
  1) 주소록 리스트
  ![image](https://github.com/user-attachments/assets/95c33696-5691-4c52-84af-b7f253a1f8ef)
  2) 사용자 추가
  ![image](https://github.com/user-attachments/assets/f58c7c6d-c058-4231-a3f3-f855c0397acc)
  3) 사용자 정보 수정
  ![image](https://github.com/user-attachments/assets/d6a8f6a6-c9f6-4e76-b67c-d0ec4e1ccb3d)

  3) 부서 추가 - 프론트 추가 중
  4) 부서 삭제 - API 연결 중
  5) 사용자 이동 - API 연결 중
  6) 사용자 삭제 - API 연결 중

- **채팅 페이지**
- ![chat (1)](https://github.com/user-attachments/assets/9fb47448-57a0-467e-b116-b7f30b68012a)
![chat (2)](https://github.com/user-attachments/assets/1a08a98b-cda8-4c98-901f-ad3703575684)
![chat (3)](https://github.com/user-attachments/assets/9c1c18a6-3582-4fab-8ae4-04b48b4e7c51)

- **설정 페이지**  
  ![001](https://github.com/user-attachments/assets/2a3d02b9-29ea-4940-9d6c-4194589e2f93)

</details>

<details>
<summary>🚀 6. 테스트 결과서</summary>

- GoogleSheets 링크 또는 내용

</details>

<details>
<summary>🚀 7. 빌드 및 배포 개요 (시스템 아키텍처)</summary>

<img width="1100" alt="system" src="https://github.com/user-attachments/assets/2a8a488d-c5a4-4346-9ac7-79a64b9e8d92" />

### 📌 사용 기술 및 도구
- **운영체제:** AWS(EC2 Ubuntu22.04)  
- **언어:** Java (Spring Boot)  
- **빌드 도구:** Gradle  
- **컨테이너화:** Docker  
- **CI/CD 도구:** Jenkins, ArgoCD  
- **배포 인프라:** Kubernetes  
- **데이터베이스:** AWS RDS(MySQL), AWS EC2(MONGODB)

### 🔄 CI/CD 개요
1. **Jenkins**: 소스 코드 관리 및 CI/CD 트리거  
2. **Jenkins**: 자동화된 빌드 및 Docker 이미지 생성, Docker Hub에 푸시  
3. **Docker Hub**: 빌드된 이미지를 저장하고 Kubernetes에서 사용  
4. **ArgoCD**: Kubernetes 클러스터에 자동 배포

</details>

---

📌 **InsaHi 레포지토리:** [GitHub - PlayData Final](https://github.com/05Daul/insahi)
