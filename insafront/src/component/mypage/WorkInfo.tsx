import styles from "@/styles/mypage/MyPage.module.css";
import React from "react";

const WorkInfo = () =>{
    return(
        <>
            <div className={styles.additionalInfo}>
                <div className={styles.infoCard}>
                    <h3>시간외근무(당월)</h3>
                    <div className={styles.infoItem}><span>근무</span> <span>2시간 50분</span></div>
                    <div className={styles.infoItem}><span>인정</span> <span>2시간 50분</span></div>
                </div>
                <div className={styles.infoCard}>
                    <h3>휴일근무(당월)</h3>
                    <div className={styles.infoItem}><span>근무</span> <span>0시간 0분</span></div>
                    <div className={styles.infoItem}><span>인정</span> <span>0시간 0분</span></div>
                </div>
            </div>
        </>
    )
}

export default WorkInfo;