import styles from "@/styles/mypage/MyPage.module.css";
import React from "react";
import {profileCardDTO} from "@/type/profilecard";
import Link from "next/link";

const ProfileCard = ({
                       employeeId,
                       name,
                       phoneNumber,
                       departmentId,
                       positionSalaryId
                     }: profileCardDTO) => {
  return (
      <div className={styles.profileWrapper}>
        <div className={styles.profileCard}>
          <div className={styles.avatar}>사진 없음</div>
          <div className={styles.profileTextBlock}>
            <h2 className={styles.profileName}>{name}</h2>
            <p><strong>사번</strong> : {employeeId}</p>
            <p><strong>부서명</strong> : {departmentId || "-"}</p>
            <p><strong>직급</strong> : {positionSalaryId || "-"}</p>
            <p><strong>전화번호</strong> : {phoneNumber}</p>
          </div>
          <Link href="/mypage/EditProfile" className={styles.editButton}>정보 변경</Link>
        </div>

        <div className={styles.quickMenu}>
          <h3>간편 메뉴</h3>
          <div className={styles.buttonContainer}>
            <Link href="/approval/submit" className={styles.menuItem}>기안하기</Link>
            <Link href="#" className={styles.menuItem}>결재 진행 중</Link>
            <Link href="#" className={styles.menuItem}>결재 완료</Link>
          </div>
        </div>
      </div>
  );
};

export default ProfileCard;
