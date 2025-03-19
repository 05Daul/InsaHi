import styles from "@/styles/mypage/MyPage.module.css";
import React, {useEffect} from "react";
import {profileCardDTO} from "@/type/profilecard";


const ProfileCard=({employeeId,name,phoneNumber,departmentId,positionSalaryId}:profileCardDTO)=>{


    return(
        <>
            <div className={styles.profileWrapper}>
                <div className={styles.profileCard}>
                    <div className={styles.profileInfo}>
                        <div className={styles.avatar}>사진 없음</div>
                        <h2>이름 : {name}</h2>
                        <p>사번 : {employeeId}</p>
                        <p>부서명 : {departmentId}</p>
                        <p>직급 : {positionSalaryId}</p>
                        <p>전화번호 : {phoneNumber}</p>
                        <button className={styles.button}>정보 변경</button>
                    </div>
                </div>
                {/* Quick Menu */}
                <div className={styles.quickMenu}>
                    <h2>간편 메뉴</h2>
                    <div className={styles.buttonContainer}>
                        <div className={styles.menuItem}>기안하기</div>
                        <div className={styles.menuItem}>결재 진행 중</div>
                        <div className={styles.menuItem}>결재 완료</div>
                    </div>
                </div>
            </div>
        </>
    )
}

export default ProfileCard;