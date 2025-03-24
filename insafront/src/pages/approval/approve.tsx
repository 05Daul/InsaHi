import {useState, ChangeEvent, FormEvent} from 'react';
import {ApprovalStatus} from '@/type/ApprovalStatus';
// // Approval 상태를 Enum처럼 정의
// export const APPROVAL_STATUS = {
//   APPROVED: 'APPROVED',
//   REJECTED: 'REJECTED'
// } as const;
// // 타입 정의 (문자열 리터럴 타입으로 제한)
// export type ApprovalStatus = typeof APPROVAL_STATUS[keyof typeof APPROVAL_STATUS];

interface ApprovalData {
  lineId: string;
  approveOrNot: ApprovalStatus; // 여기서 타입을 사용
  reason: string;
}

/*
interface ApprovalData {
  lineId: string;
  approveOrNot: 'APPROVED' | 'REJECTED';
  reason: string;
}
*/

const ApprovePage = () => {
  const [approvalData, setApprovalData] = useState<ApprovalData>({
    lineId: '',
    approveOrNot: ApprovalStatus.APPROVED,
    reason: ''
  });

  const handleInputChange = (e: ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const {name, value} = e.target;
    setApprovalData((prevData) => ({...prevData, [name]: value as ApprovalStatus}));
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();

    const {lineId, approveOrNot, reason} = approvalData;

    // 간단한 유효성 검사
    if (!lineId) {
      alert('결재 라인 ID를 입력해주세요.');
      return;
    }

    try {
      const response = await fetch('http://127.0.0.1:1005/approval/permit', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({lineId, approveOrNot, reason})
      });

      if (response.ok) {
        alert('결재가 처리되었습니다.');
        // 폼 초기화
        setApprovalData({
          lineId: '',
          approveOrNot: ApprovalStatus.APPROVED, // 폼 초기화 시에도 상수 사용
          reason: ''
        });

      } else {
        const errorData = await response.json();
        alert(`결재 처리 실패: ${errorData.message || '알 수 없는 오류'}`);
      }
    } catch (error) {
      alert(`서버 오류: ${error}`);
    }
  };

  return (
      <div>
        <h1>결재 승인/반려</h1>
        <form onSubmit={handleSubmit}>
          <div>
            <label>결재 라인 ID</label>
            <input
                type="text"
                name="lineId"
                value={approvalData.lineId}
                onChange={handleInputChange}
                placeholder="결재 라인 ID를 입력하세요"
            />
          </div>

          <div>
            <label>승인 여부</label>
            <select
                name="approveOrNot"
                value={approvalData.approveOrNot}
                onChange={handleInputChange}
            >
              <option value={ApprovalStatus.APPROVED}>승인</option>
              <option value={ApprovalStatus.REJECTED}>반려</option>
            </select>
          </div>

          {approvalData.approveOrNot === ApprovalStatus.REJECTED && (
              <div>
                <label>반려 사유</label>
                <input
                    type="text"
                    name="reason"
                    value={approvalData.reason}
                    onChange={handleInputChange}
                    placeholder="반려 사유를 입력하세요"
                />
              </div>
          )}

          <button type="submit">제출</button>
        </form>
      </div>
  );
};

export default ApprovePage;
