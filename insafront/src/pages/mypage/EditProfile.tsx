import React, {useEffect, useState} from "react";
import styles from "@/styles/mypage/EditProfile.module.css";
import EmployeeInfoAction from "@/api/mypage/employeeinfoaction";
import EmployeeInfoUpdateAction from "@/api/mypage/employeeinfoupdateaction"
import {useRouter} from "next/router";
import Link from "next/link";


function EditProfile() {
  const [employee, setEmployee] = useState<employeeInfoDTO | null>(null);
  const [employeeIdToken, setEmployeeIdToken] = useState<string | null>(null);

  const [email, setEmail] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [profileImage, setProfileImage] = useState<File | null>(null);
  const [previewImage, setPreviewImage] = useState<string | null>(null);
  const router = useRouter();
  const formattedHireDate = employee?.hireDate
      ? new Date(employee.hireDate).toISOString().slice(0, 10) // yyyy-mm-dd
      : "-";


  useEffect(() => {
    // 클라이언트에서만 실행되도록 보장
    if (typeof window !== "undefined") {
      const storedEmployeeId = localStorage.getItem("employeeId") || "defaultId";
      setEmployeeIdToken(storedEmployeeId);
    }
  }, []);


  useEffect(() => {
    if (employeeIdToken) {
      const fetchData = async () => {
        const data = await EmployeeInfoAction(employeeIdToken);
        if (data) {
          setEmployee(data);
          setEmail(data.email ?? "");
          setPhoneNumber(data.phoneNumber ?? "");
        } else {
          console.warn("No data found.");
        }
      };
      fetchData();
    }
  }, [employeeIdToken]);

  //수정버튼
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!employee) return;

    const formData = new FormData();
    formData.append("email", email);
    formData.append("phoneNumber", phoneNumber);
    if (profileImage) {
      formData.append("profileImage", profileImage); // 파일도 함께 전송
    }
    for (let [key, value] of formData.entries()) {
      console.log("🧪 formData key/value:", key, value);
    }


    try {
      const result = await EmployeeInfoUpdateAction(employee.employeeId, formData);
      if (result) {
        alert("정보가 업데이트되었습니다.");
      } else {
        alert("업데이트에 실패했습니다.");
      }
      router.push("/mypage/MyPage")
    } catch (error) {
      console.error("Update Error:", error);
      alert("업데이트 중 오류가 발생했습니다.");
    }
  };
  //image 변경
  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      setProfileImage(file);
      setPreviewImage(URL.createObjectURL(file));
    }
  };


  return (
      <div className={styles.container}>
        <h2 className={styles.title}>개인정보 수정</h2>
        <form onSubmit={handleSubmit} className={styles.form}>

          {/* 프로필 사진 업로드 */}
          <div className={styles.formGroup}>
            <label>프로필 사진</label>
            {previewImage ? (
                <img src={previewImage} alt="미리보기" className={styles.profileImage}/>
            ) : employee?.profileImage ? (
                <img src={employee.profileImage} alt="현재 이미지" className={styles.profileImage}/>
            ) : (
                <div>사진 없음</div>
            )}
            <input type="file" accept="image/*" onChange={handleImageChange}/>
          </div>

          {/* 이름 */}
          <div className={styles.formGroup}>
            <label>이름</label>
            <input type="text" name="name" value={employee?.name ?? ""} disabled/>
          </div>

          {/* 아이디 */}
          <div className={styles.formGroup}>
            <label>아이디</label>
            <input type="text" name="employeeId" value={employee?.employeeId ?? ""} disabled/>
          </div>


          {/* 이메일 */}
          <div className={styles.formGroup}>
            <label>이메일</label>
            <input type="text" name="email" value={email}
                   onChange={(e) => setEmail(e.target.value)}/>
          </div>

          {/* 전화번호 */}
          <div className={styles.formGroup}>
            <label>전화번호</label>
            <input type="text" name="phoneNumber" value={phoneNumber}
                   onChange={(e) => setPhoneNumber(e.target.value)}/>
          </div>

          {/* 부서 ID */}
          <div className={styles.formGroup}>
            <label>부서 ID</label>
            <input type="text" name="departmentName" value={employee?.departmentName ?? " - "}
                   disabled/>
          </div>

          {/* 입사일 */}
          <div className={styles.formGroup}>
            <label>입사일</label>
            <input type="text" name="hireDate" value={formattedHireDate ?? " - "}
                   disabled/>
          </div>


          <button type="submit" className={styles.submitButton}>수정 완료</button>
          <Link href="/mypage/UpdatePassword" className={styles.changePassword}>
            <b>비밀번호 변경</b>
          </Link>
        </form>

      </div>
  );
};

export default EditProfile;