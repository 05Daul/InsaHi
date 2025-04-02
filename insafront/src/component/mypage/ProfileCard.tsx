import styles from "@/styles/mypage/MyPage.module.css";
import React from "react";
import {profileCardDTO} from "@/type/profilecard";
import Link from "next/link";
import {useQuery} from "@tanstack/react-query";
import {fetchAttendanceRecords} from "@/services/attendanceService";
import AttendanceAction from "@/services/attendanceAction";

const ProfileCard = ({
                       employeeId,
                       name,
                       phoneNumber,
                       departmentName,
                       email,
                       profileImage,
                       hireDate
                     }: profileCardDTO) => {
  console.log("pro pro pro img img img img img", profileImage)
  const {
    data: attendanceRecords = [],
  } = useQuery({
    queryKey: ['attendanceRecords', employeeId],
    queryFn: () => fetchAttendanceRecords(employeeId!),
    enabled: !!employeeId, // employeeId가 있을 때만 실행
  });
  console.log("입사일:", hireDate);
  const formattedHireDate = hireDate ? new Date(hireDate).toLocaleDateString("ko-KR") : "-";

  return (
      <div className={styles.profileWrapper}>
        <div className={styles.profileCard}>
          <div className={styles.avatar}>
            {profileImage ? (
                <img src={profileImage} alt="프로필 사진" className={styles.avatarImage}/>
            ) : (
                "사진 없음"
            )}
          </div>
          <div className={styles.profileTextBlock}>
            <h2 className={styles.profileName}>{name}</h2>
            <p><strong>사번</strong> : {employeeId}</p>
            <p><strong>부서명</strong> : {departmentName || "-"}</p>
            <p><strong>이메일</strong> : {email || "-"}</p>
            <p><strong>전화번호</strong> : {phoneNumber}</p>
            <p><strong>입사일</strong> : {formattedHireDate || "-"}</p>

          </div>
          <Link href="/mypage/EditProfile" className={styles.editButton}>정보 변경</Link>
        </div>

        <div className={styles.quickMenu}>
          <h3>간편 메뉴</h3>
          <div className={styles.buttonContainer}>
            <Link href="/approval/submit" className={styles.menuItem}>기안하기</Link>
            <Link href="/approval/file/[approvalFileId]]" className={styles.menuItem}>전자결재 보기</Link>
            <AttendanceAction employeeId={employeeId!} attendanceRecords={attendanceRecords}/>
          </div>
        </div>
      </div>
  );
};

export default ProfileCard;
