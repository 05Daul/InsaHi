import React, {useEffect, useState} from "react";
import styles from "@/styles/mypage/MyPage.module.css";
import {profileCardDTO} from "@/type/profilecard";
import AttendanceCard from "@/component/mypage/AttendanceCard";
import WorkInfo from "@/component/mypage/WorkInfo";
import ProfileCard from "@/component/mypage/ProfileCard";
import Calendar from "@/component/mypage/Calendar";
import profileCardAction from "@/api/mypage/profilecardaction";

const MyPage = () => {

  const [userData, setUserData] = useState<profileCardDTO | null>(null);
  const [employeeIdToken, setEmployeeIdToken] = useState<string | null>(null); // 로그인한 사용자의 ID 가져오기


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
        const data = await profileCardAction(employeeIdToken);
        if (data) {
          setUserData(data);
        } else {
          console.warn("No data found.");
        }
      };
      fetchData();
    }
  }, [employeeIdToken]);

  return (
      <div className={styles.container}>
        <div className={styles.topSection}>
          {/* Profile & Quick Menu Section */}
          {userData ? <ProfileCard{...userData}/> : <p>Loading</p>}

          {/* Calendar Section */}
          <div className={styles.calendarCard}>
            <Calendar/>
          </div>
        </div>

        <div className={styles.attendanceWrapper}>
          {/* Attendance Section */}
          <AttendanceCard/>

          {/* Additional Work Info */}
          <WorkInfo/>
        </div>

        <div className={styles.bottomSection}>
          {/* Time Buttons */}
          {/*<ClockButton />*/}

        </div>
      </div>
  );
};

export default MyPage;
