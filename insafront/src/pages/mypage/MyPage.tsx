import React, {useEffect, useState} from "react";
import styles from "@/styles/mypage/MyPage.module.css";
import {profileCardDTO} from "@/type/profilecard";
import ProfileCard from "@/component/mypage/ProfileCard";
import Calendar from "@/component/mypage/Calendar";
import profileCardAction from "@/api/mypage/profilecardaction";
import CalendarAction, {CalendarDTO} from "@/api/mypage/calendaraction";


const MyPage = () => {

  const [userData, setUserData] = useState<profileCardDTO | null>(null);
  const [calendarData, setCalendarData] = useState<CalendarDTO[]>([]);
  const [employeeId, setEmployeeId] = useState<string | null>(null); // 로그인한 사용자의 ID 가져오기
  useEffect(() => {
    // 클라이언트에서만 실행되도록 보장
    if (typeof window !== "undefined") {
      const storedEmployeeId = localStorage.getItem("employeeId");
      setEmployeeId(storedEmployeeId);
    }
  }, [employeeId]);


  //calendar 출력
  useEffect(() => {
    if (employeeId) {
      const fetchData = async () => {
        const data = await CalendarAction(employeeId, "APPROVED"); // "APPROVED" 고정
        if (data) {
          setCalendarData(data);
          console.log("setCalendarData(data);")
        } else {
          console.log("setCalendarData([]);")
          setCalendarData([]);
          // console.warn("No data found.");
        }
      };
      fetchData();
    }
  }, [employeeId]); //status 어케 처리함?

  //ProfileCard 출력
  useEffect(() => {
    if (employeeId) {
      const fetchData = async () => {
        const data = await profileCardAction(employeeId);
        if (data) {
          setUserData(data);
        } else {
          console.warn("No data found.");
        }
      };
      fetchData();
    }
  }, [employeeId]);

  return (
      <div className={styles.container}>
        <div className={styles.topSection}>
          {/* Profile & Quick Menu Section */}
          {userData ? <ProfileCard{...userData}/> : <p>ProfileCard Loading~~~~~~~~~~~~~~~~</p>}

          {/* Calendar Section */}

          <div className={styles.calendarCard}>
            {calendarData.length >= 0 ? (
                <Calendar leaveList={calendarData}/>
            ) : (
                <p>Calendar Loading ~~</p>
            )}
          </div>
        </div>

        <div className={styles.attendanceWrapper}>
        </div>

      </div>
  );
};

export default MyPage;
