import React, { useState } from 'react';
import styles from '../../styles/leave.module.css'; // ✅ CSS 모듈 import

interface LeaveRequestFormProps {
    onSubmit: (leave: LeaveData) => void;
}

interface LeaveData {
    id: number;
    applicant: string;
    startDate: string;
    endDate: string;
    reason: string;
    status: '대기' | '승인' | '반려';
}

const LeaveRequestForm: React.FC<LeaveRequestFormProps> = ({ onSubmit }) => {
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [reason, setReason] = useState('');

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        if (!startDate || !endDate || !reason) {
            alert('모든 필드를 입력하세요.');
            return;
        }

        const newLeave: LeaveData = {
            id: Date.now(),
            applicant: '홍길동',
            startDate,
            endDate,
            reason,
            status: '대기',
        };

        onSubmit(newLeave);
        setStartDate('');
        setEndDate('');
        setReason('');
    };

    return (
        <div className={styles['leave-request-section']}>
            <h2 className={styles['section-title']}>휴가 신청</h2>
            <form onSubmit={handleSubmit} className={styles['space-y-4']}>
                <div>
                    <label>시작일</label>
                    <input
                        type="date"
                        className={styles.input}
                        value={startDate}
                        onChange={(e) => setStartDate(e.target.value)}
                    />
                </div>
                <div>
                    <label>종료일</label>
                    <input
                        type="date"
                        className={styles.input}
                        value={endDate}
                        onChange={(e) => setEndDate(e.target.value)}
                    />
                </div>
                <div>
                    <label>사유</label>
                    <textarea
                        className={styles['input-textarea']}
                        value={reason}
                        onChange={(e) => setReason(e.target.value)}
                    />
                </div>
                <button type="submit" className={styles['btn-primary']}>
                    휴가 신청
                </button>
            </form>
        </div>
    );
};

export default LeaveRequestForm;