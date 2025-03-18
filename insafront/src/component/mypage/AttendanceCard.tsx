import styles from "@/styles/mypage/MyPage.module.css";
import React, {useState} from "react";


const AttendanceCard = () => {

    const [clockInTime, setClockInTime] = useState<string | null>(null);
    const [clockOutTime, setClockOutTime] = useState<string | null>(null);
    return(
        <>
            <div className={styles.attendanceCard}>
                <div className={styles.attendanceHeader}>개인 근태 현황</div>
                <div className={styles.attendanceBody}>
                    <div className={styles.attendanceItem}>출근 시간<br/>{clockInTime || "--:--"}</div>
                    <div className={styles.attendanceItem}>퇴근 시간<br/>{clockOutTime || "--:--"}</div>
                </div>
            </div>
        </>
    )
}

export default AttendanceCard;