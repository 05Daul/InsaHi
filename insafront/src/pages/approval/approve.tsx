import {useState, ChangeEvent, FormEvent} from 'react';

interface ApprovalData {
  lineId: string;
  approveOrNot: 'APPROVED' | 'REJECTED';
  reason: string;
}

const ApprovePage = () => {
  const [approvalData, setApprovalData] = useState<ApprovalData>({
    lineId: '',
    approveOrNot: 'APPROVED',
    reason: ''
  });

  const handleInputChange = (e: ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const {name, value} = e.target;
    setApprovalData((prevData) => ({...prevData, [name]: value}));
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    const {lineId, approveOrNot, reason} = approvalData;

    const response = await fetch(`http://127.0.0.1:1006/approval/permit?lineId=${lineId}&approveOrNot=${approveOrNot}&reason=${reason}`, {
      method: 'GET'
    });

    if (response.ok) {
      alert('결재가 처리되었습니다.');
    } else {
      alert('결재 처리 실패');
    }
  };

  return (
      <div>
        <h1>결재 승인/반려</h1>
        <form onSubmit={handleSubmit}>
          <div>
            <label>결재 라인 ID</label>
            <input type="text" name="lineId" value={approvalData.lineId}
                   onChange={handleInputChange}/>
          </div>
          <div>
            <label>승인 여부</label>
            <select name="approveOrNot" value={approvalData.approveOrNot}
                    onChange={handleInputChange}>
              <option value="APPROVED">승인</option>
              <option value="REJECTED">반려</option>
            </select>
          </div>
          {approvalData.approveOrNot === 'REJECTED' && (
              <div>
                <label>반려 사유</label>
                <input type="text" name="reason" value={approvalData.reason}
                       onChange={handleInputChange}/>
              </div>
          )}
          <button type="submit">제출</button>
        </form>
      </div>
  );
};

export default ApprovePage;
