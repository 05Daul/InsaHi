import React from 'react';
import styles from '../../styles/leave.module.css'; // ✅ CSS 모듈 import

interface LeaveData {
    id: number;
    applicant: string;
    startDate: string;
    endDate: string;
    reason: string;
    status: '대기' | '승인' | '반려';
}

interface AdminLeaveApprovalProps {
    leaves: LeaveData[];
    onApprove: (id: number) => void;
    onReject: (id: number) => void;
}

const AdminLeaveApproval: React.FC<AdminLeaveApprovalProps> = ({
                                                                   leaves,
                                                                   onApprove,
                                                                   onReject,
                                                               }) => {
    return (
        <div className={styles['admin-leave-section']}>
            <h2 className={styles['section-title']}>휴가 승인/반려 처리 (관리자)</h2>
            <table className="w-full table-auto border">
                <thead>
                <tr>
                    <th>신청자</th>
                    <th>시작일</th>
                    <th>종료일</th>
                    <th>사유</th>
                    <th>상태</th>
                    <th>처리</th>
                </tr>
                </thead>
                <tbody>
                {leaves.length === 0 ? (
                    <tr>
                        <td colSpan={6} className="text-center">
                            신청된 휴가가 없습니다.
                        </td>
                    </tr>
                ) : (
                    leaves.map((leave) => (
                        <tr key={leave.id} className="border-t">
                            <td>{leave.applicant}</td>
                            <td>{leave.startDate}</td>
                            <td>{leave.endDate}</td>
                            <td>{leave.reason}</td>
                            <td>{leave.status}</td>
                            <td>
                                {leave.status === '대기' ? (
                                    <>
                                        <button
                                            className={styles['btn-approve']}
                                            onClick={() => onApprove(leave.id)}
                                        >
                                            승인
                                        </button>
                                        <button
                                            className={styles['btn-reject']}
                                            onClick={() => onReject(leave.id)}
                                        >
                                            반려
                                        </button>
                                    </>
                                ) : (
                                    <span>{leave.status}</span>
                                )}
                            </td>
                        </tr>
                    ))
                )}
                </tbody>
            </table>
        </div>
    );
};

export default AdminLeaveApproval;