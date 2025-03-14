import React, { useState } from 'react';
import MainLayout from '@/component/MainLayout'

// 샘플 데이터 타입
type ApprovalItem = {
    id: number;
    type: string;
    title: string;
    department: string;
    drafter: string;
    draftDate: string;
    status: string;
};

const sampleApprovals: ApprovalItem[] = [
    {
        id: 1,
        type: '결재의뢰서',
        title: '[플레이데이터] 빔프로젝터 구매 2건',
        department: '전략사업본부',
        drafter: '김승철 부장',
        draftDate: '2025-01-24',
        status: '진행중',
    },
];

const sampleLeaves = [
    {
        id: 1,
        type: '연차휴가',
        reason: '개인 사유',
        dates: ['2025-03-10', '2025-03-11'],
        status: '승인대기',
    },
];

const ApprovalListPage = () => {
    const [selectedApproval, setSelectedApproval] = useState<ApprovalItem | null>(null);
    const [approvals, setApprovals] = useState(sampleApprovals);
    const [selectedLeaveDates, setSelectedLeaveDates] = useState<string[]>([]);
    const [leaveReason, setLeaveReason] = useState('');
    const [leaves, setLeaves] = useState(sampleLeaves);

    const handleDetailClick = (item: ApprovalItem) => {
        setSelectedApproval(item);
    };

    const handleApprove = () => {
        if (!selectedApproval) return;
        alert(`${selectedApproval.title} 승인 처리 완료`);
        setSelectedApproval(null);
        setApprovals((prev) =>
            prev.map((item) =>
                item.id === selectedApproval.id ? { ...item, status: '완료' } : item
            )
        );
    };

    const handleReject = () => {
        if (!selectedApproval) return;
        alert(`${selectedApproval.title} 반려 처리 완료`);
        setSelectedApproval(null);
        setApprovals((prev) =>
            prev.map((item) =>
                item.id === selectedApproval.id ? { ...item, status: '반려' } : item
            )
        );
    };

    const handleDateClick = (date: string) => {
        if (selectedLeaveDates.includes(date)) {
            setSelectedLeaveDates(selectedLeaveDates.filter(d => d !== date));
        } else {
            setSelectedLeaveDates([...selectedLeaveDates, date]);
        }
    };

    const handleLeaveSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (selectedLeaveDates.length === 0 || !leaveReason) {
            alert('날짜와 사유를 입력하세요.');
            return;
        }
        const newLeave = {
            id: leaves.length + 1,
            type: '연차휴가',
            reason: leaveReason,
            dates: selectedLeaveDates,
            status: '승인대기',
        };
        setLeaves([...leaves, newLeave]);
        alert('연차 신청 완료');
        setSelectedLeaveDates([]);
        setLeaveReason('');
    };

    return (
            <div className="page-container">
                <h1 className="page-title">결재 현황</h1>

                {/* 진행 중인 결재 */}
                <section className="approval-section">
                    <h2 className="section-title">진행중인 결재</h2>
                    <table className="approval-table">
                        <thead>
                        <tr>
                            <th>No.</th>
                            <th>결재양식</th>
                            <th>기안 제목</th>
                            <th>기안부서</th>
                            <th>기안자</th>
                            <th>기안일자</th>
                            <th>상태</th>
                            <th>상세</th>
                        </tr>
                        </thead>
                        <tbody>
                        {approvals.map((item, index) => (
                            <tr key={item.id}>
                                <td>{index + 1}</td>
                                <td>{item.type}</td>
                                <td>{item.title}</td>
                                <td>{item.department}</td>
                                <td>{item.drafter}</td>
                                <td>{item.draftDate}</td>
                                <td>
                    <span
                        className={
                            item.status === '진행중'
                                ? 'status-progress'
                                : item.status === '완료'
                                    ? 'status-complete'
                                    : 'status-reject'
                        }
                    >
                      {item.status}
                    </span>
                                </td>
                                <td>
                                    <button className="btn-secondary" onClick={() => handleDetailClick(item)}>
                                        상세 보기
                                    </button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </section>

                {selectedApproval && (
                    <div className="detail-modal">
                        <h2>결재 상세 정보</h2>
                        <p><strong>제목:</strong> {selectedApproval.title}</p>
                        <p><strong>기안부서:</strong> {selectedApproval.department}</p>
                        <p><strong>기안자:</strong> {selectedApproval.drafter}</p>
                        <p><strong>기안일자:</strong> {selectedApproval.draftDate}</p>

                        <div className="button-group">
                            <button className="btn-approve" onClick={handleApprove}>
                                승인
                            </button>
                            <button className="btn-reject" onClick={handleReject}>
                                반려
                            </button>
                            <button className="btn-cancel" onClick={() => setSelectedApproval(null)}>
                                닫기
                            </button>
                        </div>
                    </div>
                )}

                {/* 연차 신청 폼 */}
                <section className="leave-request-section">
                    <h2 className="section-title">연차/휴가 신청</h2>
                    <form onSubmit={handleLeaveSubmit} className="form-container">
                        <div className="form-group">
                            <label htmlFor="leave-reason">사유</label>
                            <textarea
                                id="leave-reason"
                                className="input-textarea"
                                placeholder="사유를 입력하세요"
                                value={leaveReason}
                                onChange={(e) => setLeaveReason(e.target.value)}
                            />
                        </div>

                        <div className="calendar-container">
                            <h3>날짜 선택</h3>
                            <div className="calendar-grid">
                                {['2025-03-10', '2025-03-11', '2025-03-12', '2025-03-13'].map(date => (
                                    <div
                                        key={date}
                                        onClick={() => handleDateClick(date)}
                                        className={`calendar-cell ${selectedLeaveDates.includes(date) ? 'selected' : ''}`}
                                    >
                                        {date}
                                    </div>
                                ))}
                            </div>
                        </div>

                        <button type="submit" className="btn-primary">신청하기</button>
                    </form>
                </section>

                {/* 신청 내역 */}
                <section className="leave-history-section">
                    <h2 className="section-title">연차 신청 내역</h2>
                    <table className="approval-table">
                        <thead>
                        <tr>
                            <th>No.</th>
                            <th>휴가 종류</th>
                            <th>사유</th>
                            <th>날짜</th>
                            <th>상태</th>
                        </tr>
                        </thead>
                        <tbody>
                        {leaves.map((leave, index) => (
                            <tr key={leave.id}>
                                <td>{index + 1}</td>
                                <td>{leave.type}</td>
                                <td>{leave.reason}</td>
                                <td>{leave.dates.join(', ')}</td>
                                <td>{leave.status}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </section>
            </div>
    );
};

export default ApprovalListPage;