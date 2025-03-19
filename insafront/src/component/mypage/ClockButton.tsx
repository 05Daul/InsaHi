import exp from "node:constants";
import styles from "@/styles/mypage/MyPage.module.css";
import React from "react";


interface ClockButtonsProps {
    setClockInTime: React.Dispatch<React.SetStateAction<string | null>>;
    setClockOutTime: React.Dispatch<React.SetStateAction<string | null>>;
}

const ClockButton=({ setClockInTime, setClockOutTime }:ClockButtonsProps)=>{
    const handleClockIn = () => setClockInTime(new Date().toLocaleTimeString());
    const handleClockOut = () => setClockOutTime(new Date().toLocaleTimeString());
    return(
        <>
            <div className={styles.inputCard}>
                <h2>근무 기록 입력</h2>
                <button className={styles.button} onClick={handleClockIn}>출근 시간 입력</button>
                <button className={styles.button} onClick={handleClockOut}>퇴근 시간 입력</button>
            </div>
        </>
    )
}

export default ClockButton;