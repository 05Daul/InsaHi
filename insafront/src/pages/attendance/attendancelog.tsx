import React, { useState } from 'react';
import styles from '../../styles/attendancelog.module.css';

type WorkLogItem = {
    date: string;
    employee: string;
    clockIn: string | null;
    clockOut: string | null;
    workTime: string;
    standardTime: string;
    difference: string;
};

const AttendanceLog: React.FC = () => {
    const [workLogs, setWorkLogs] = useState<WorkLogItem[]>([
        {
            date: '2025-03-15',
            employee: '유하나',
            clockIn: null,
            clockOut: null,
            workTime: '0시간 0분',
            standardTime: '9시간 0분',
            difference: '0시간 0분',
        },
    ]);

    const handleClockIn = () => {
        const today = new Date().toISOString().slice(0, 10);
        setWorkLogs((prevLogs) => {
            const updatedLogs = [...prevLogs];
            const todayLogIndex = updatedLogs.findIndex((log) => log.date === today);

            if (todayLogIndex >= 0) {
                updatedLogs[todayLogIndex].clockIn = new Date().toLocaleTimeString();
            } else {
                updatedLogs.push({
                    date: today,
                    employee: '유하나',
                    clockIn: new Date().toLocaleTimeString(),
                    clockOut: null,
                    workTime: '0시간 0분',
                    standardTime: '9시간 0분',
                    difference: '0시간 0분',
                });
            }

            return updatedLogs;
        });
    };

    const handleClockOut = () => {
        const today = new Date().toISOString().slice(0, 10);

        setWorkLogs((prevLogs) => {
            const updatedLogs = [...prevLogs];
            const todayLogIndex = updatedLogs.findIndex((log) => log.date === today);

            if (todayLogIndex >= 0 && updatedLogs[todayLogIndex].clockIn) {
                const clockInTime = new Date(`${today}T${updatedLogs[todayLogIndex].clockIn}`);
                const clockOutTime = new Date();
                const diffMs = clockOutTime.getTime() - clockInTime.getTime();
                const diffMins = Math.floor(diffMs / (1000 * 60));
                const hours = Math.floor(diffMins / 60);
                const minutes = diffMins % 60;

                const standardMins = 540;
                const differenceMins = diffMins - standardMins;
                const diffHours = Math.floor(Math.abs(differenceMins) / 60);
                const diffMinutes = Math.abs(differenceMins) % 60;

                updatedLogs[todayLogIndex].clockOut = clockOutTime.toLocaleTimeString();
                updatedLogs[todayLogIndex].workTime = `${hours}시간 ${minutes}분`;
                updatedLogs[todayLogIndex].difference = `${differenceMins >= 0 ? '' : '-'}${diffHours}시간 ${diffMinutes}분`;
            }

            return updatedLogs;
        });
    };

    return (
            <div className={styles.pageWrapper}>
                <h2 className={styles.title}>일별 근무시간 현황</h2>

                <div className={styles.buttonWrapper}>
                    <button onClick={handleClockIn} className={styles.button}>출근</button>
                    <button onClick={handleClockOut} className={styles.button}>퇴근</button>
                </div>

                <table className={styles.workLogTable}>
                    <thead>
                    <tr>
                        <th>일자</th>
                        <th>사원명</th>
                        <th>출근시간</th>
                        <th>퇴근시간</th>
                        <th>근무시간</th>
                        <th>표준근무시간</th>
                        <th>차이</th>
                    </tr>
                    </thead>
                    <tbody>
                    {workLogs.map((log, index) => (
                        <tr key={index}>
                            <td>{log.date}</td>
                            <td>{log.employee}</td>
                            <td>{log.clockIn || '-'}</td>
                            <td>{log.clockOut || '-'}</td>
                            <td>{log.workTime}</td>
                            <td>{log.standardTime}</td>
                            <td>{log.difference}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
    );
};

export default AttendanceLog;