import React, { useState } from 'react';
import LeaveRequestForm from './leeaverequestform'
import MyLeaveList from './myleavelist';
import AdminLeaveApproval from './adminleaveapproval'
import styles from '../../styles/leave.module.css'; // ✅ CSS 모듈 import

const LeavePage = () => {
    const [leaveList, setLeaveList] = useState<any[]>([]);

    const handleSubmit = (leave: any) => {
        setLeaveList([...leaveList, leave]);
    };

    const handleApprove = (id: number) => {
        setLeaveList(
            leaveList.map((leave) =>
                leave.id === id ? { ...leave, status: '승인' } : leave
            )
        );
    };

    const handleReject = (id: number) => {
        setLeaveList(
            leaveList.map((leave) =>
                leave.id === id ? { ...leave, status: '반려' } : leave
            )
        );
    };

    return (
            <div className={`${styles['leave-container']} p-6 space-y-10`}>
                <LeaveRequestForm onSubmit={handleSubmit} />
                <MyLeaveList leaves={leaveList.filter((leave) => leave.applicant === '홍길동')} />
                <AdminLeaveApproval
                    leaves={leaveList}
                    onApprove={handleApprove}
                    onReject={handleReject}
                />
            </div>
    );
};

export default LeavePage;