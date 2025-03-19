import React, { useState, useEffect } from "react";
import { NextPage } from "next";
import styles from "@/styles/mypage/MyPage.module.css";
import { profileCardDTO } from "@/type/profilecard";
import AttendanceCard from "@/component/mypage/AttendanceCard";
import WorkInfo from "@/component/mypage/WorkInfo";
import ClockButton from "@/component/mypage/ClockButton";
import ProfileCard from "@/component/mypage/ProfileCard";
import Calendar from "@/component/mypage/Calendar";
import profileCardAction from "@/api/mypageaction";

const MyPage = () => {

    const [userData, setUserData] = useState<profileCardDTO | null>(null);
    const employeeId = localStorage.getItem("employeeId") || "defaultID"; // 로그인한 사용자의 ID 가져오기

    // console.log(employeeId)

    // useEffect(() => {
    //     fetcher("/mypage/MyPage")
    //         .then((data)=>setUserData(data))
    //         .catch(console.error);
    // }, []);
    useEffect(() => {
        const fetchData = async () => {
            const data = await profileCardAction(employeeId);
            console.log("datadata "+data);
            if(data){
                setUserData(data);
            }
            else {
                console.warn("No data")
            }
        };
        fetchData();
    }, [employeeId]);

    return (
        <div className={styles.container}>
            <div className={styles.topSection}>
                {/* Profile & Quick Menu Section */}
                {userData ? <ProfileCard{...userData}/> : <p>Loading</p>}

                {/* Calendar Section */}
                <div className={styles.calendarCard}>
                    <h2>근태 달력</h2>
                    <Calendar/>
                </div>
            </div>

            <div className={styles.attendanceWrapper}>
                {/* Attendance Section */}
                <AttendanceCard />

                {/* Additional Work Info */}
                <WorkInfo />
            </div>

            <div className={styles.bottomSection}>
                {/* Time Buttons */}
                {/*<ClockButton />*/}

            </div>
        </div>
    );
};

export default MyPage;
