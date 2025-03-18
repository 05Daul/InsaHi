import React from 'react';
import styles from '../../styles/leave.module.css'

interface LeaveData {
    id: number;
    applicant: string;
    startDate: string;
    endDate: string;
    reason: string;
    status: '대기' | '승인' | '반려';
}

interface MyLeaveListProps {
    leaves: LeaveData[];
}

const MyLeaveList: React.FC<MyLeaveListProps> = ({ leaves }) => {
    return (
        <div className={styles['leave-history-section']}>
            <h2 className={styles['section-title']}>내 휴가 신청 내역</h2>
            <table className="w-full table-auto border">
                <thead>
                <tr>
                    <th>시작일</th>
                    <th>종료일</th>
                    <th>사유</th>
                    <th>상태</th>
                </tr>
                </thead>
                <tbody>
                {leaves.length === 0 ? (
                    <tr>
                        <td colSpan={4} className="text-center">
                            신청한 휴가가 없습니다.
                        </td>
                    </tr>
                ) : (
                    leaves.map((leave) => (
                        <tr key={leave.id} className="border-t">
                            <td>{leave.startDate}</td>
                            <td>{leave.endDate}</td>
                            <td>{leave.reason}</td>
                            <td>{leave.status}</td>
                        </tr>
                    ))
                )}
                </tbody>
            </table>
        </div>
    );
};

export default MyLeaveList;