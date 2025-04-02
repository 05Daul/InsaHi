// pages/mypage/ChangePassword.tsx
import React, {useEffect, useState} from "react";
import {useRouter} from "next/router";
import styles from "@/styles/mypage/EditProfile.module.css";
import UpdatePasswordAction from "@/api/mypage/updatepasswordaction"; // 스타일 재사용

function UpdatePassword() {
  const [employeeId, setEmployeeId] = useState("");
  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const router = useRouter();

  useEffect(() => {
    if (typeof window !== "undefined") {
      const id = localStorage.getItem("employeeId") || "";
      setEmployeeId(id);
    }
  }, []);

  const handleChangePassword = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const result = await UpdatePasswordAction(employeeId, currentPassword, newPassword);
      if (result) {
        alert("비밀번호가 변경되었습니다.");
      } else {
        alert("비밀번호 변경에 실패했습니다.");
      }
      router.push("/mypage/MyPage");
    } catch (error) {
      console.error("Password Change Error:", error);
      alert("비밀번호 변경에 실패했습니다.");
    }
  };

  return (
      <div className={styles.container}>
        <h2 className={styles.title}>비밀번호 변경</h2>
        <form onSubmit={handleChangePassword} className={styles.form}>
          <div className={styles.formGroup}>
            <label>현재 비밀번호</label>
            <input
                type="password"
                value={currentPassword}
                onChange={(e) => setCurrentPassword(e.target.value)}
                required
            />
          </div>

          <div className={styles.formGroup}>
            <label>새로운 비밀번호</label>
            <input
                type="password"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                required
            />
          </div>

          <button type="submit" className={styles.submitButton}>
            비밀번호 변경
          </button>
        </form>
      </div>
  );
}

export default UpdatePassword;
