import {useState, useEffect} from 'react';
import {useRouter} from 'next/router';
import styles from '@/styles/approval/ApprovalDetail.module.css';
import {ApprovalStatus} from '@/type/ApprovalStatus';
import StatusBadge from '@/component/approval/StatusBadge';

interface File {
  approvalFileNo: string;
  originalFilename: string;
  storeFilename: string;
}

interface ApprovalLine {
  id: string;
  approvalOrder: number;
  employeeId: string;
  approvalStatus: ApprovalStatus;
}

interface ApprovalFile {
  id: string;
  name: string;
  text: string;
  companyCode: string;
  employeeId: string;
  status: ApprovalStatus;
  files: File[];
  approvalLines: ApprovalLine[];
}

const FilePage = () => {
  const router = useRouter();
  const {approvalFileId, menu} = router.query;

  const [approvalFile, setApprovalFile] = useState<ApprovalFile | null>(null);
  const [employeeIdToken, setEmployeeIdToken] = useState<string | null>(null);
  const [reason, setReason] = useState<string>('');

  useEffect(() => {
    const storedEmployeeId = localStorage.getItem('employeeId') || 'defaultId';
    setEmployeeIdToken(storedEmployeeId);
  }, []);

  useEffect(() => {
    if (!approvalFileId) return;
    const fetchApprovalFileDetails = async () => {
      const response = await fetch(`http://127.0.0.1:1006/approval/file/${approvalFileId}`);
      const data = await response.json();
      setApprovalFile(data);
    };
    fetchApprovalFileDetails();
  }, [approvalFileId]);

  if (!approvalFile) return <div>Loading...</div>;

  const referenceLines = approvalFile.approvalLines.filter(line => line.approvalOrder === 0);
  const approverLines = approvalFile.approvalLines.filter(line => line.approvalOrder !== 0);

  const handleApproval = async (lineId: string, status: ApprovalStatus) => {
    const payload = {lineId, approveOrNot: status, reason};
    const response = await fetch(`http://127.0.0.1:1006/approval/permit`, {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(payload)
    });
    if (response.ok) router.push('/approval/documents');
  };

  return (
      <div className={styles.approvalDetailPageContainer}>
        <h1 className={styles.approvalDetailPageTitle}>기안서</h1>

        {/* 결재라인 테이블 */}
        <table className={styles.approvalLineTable}>
          <thead>
          <tr>
            <th>구분</th>
            {approverLines.map(line => (
                <th key={line.id}>결재자 {line.approvalOrder}</th>
            ))}
          </tr>
          </thead>
          <tbody>
          <tr>
            <td>사번</td>
            {approverLines.map(line => (<td key={line.id}>{line.employeeId}</td>))}
          </tr>
          <tr>
            <td>상태</td>
            {approverLines.map(line => (
                <td key={line.id}>
                  {line.employeeId === employeeIdToken && line.approvalStatus === ApprovalStatus.PENDING && menu === '3' ? (
                      <>
                    <textarea
                        className={styles.approvalDetailtextarea}
                        value={reason}
                        onChange={(e) => setReason(e.target.value)}
                        placeholder="사유 입력"
                    />
                        <button className={styles.approvalDetailbutton}
                                onClick={() => handleApproval(line.id, ApprovalStatus.APPROVED)}>승인
                        </button>
                        <button className={styles.approvalDetailbutton}
                                onClick={() => handleApproval(line.id, ApprovalStatus.REJECTED)}>반려
                        </button>
                      </>
                  ) : (
                      <StatusBadge status={line.approvalStatus}/>
                  )}
                </td>
            ))}
          </tr>
          </tbody>
        </table>

        {/* 문서 메타 정보 테이블 */}
        <table className={styles.approvalDetailMetaTable}>
          <tbody>
          <tr>
            <th>문서 제목</th>
            <td>{approvalFile.name}</td>
            <th>기안자</th>
            <td>{approvalFile.employeeId}</td>
          </tr>
          <tr>
            <th>회사 코드</th>
            <td>{approvalFile.companyCode}</td>
            <th>상태</th>
            <td><StatusBadge status={approvalFile.status}/></td>
          </tr>
          </tbody>
        </table>

        {/* 참조자 목록 */}
        <table className={styles.approvalDetailMetaTable}>
          <thead>
          <tr>
            <th>참조자</th>
            <th>상태</th>
          </tr>
          </thead>
          <tbody>
          {referenceLines.length === 0 ? (
              <tr>
                <td colSpan={2}>참조자가 없습니다.</td>
              </tr>
          ) : (
              referenceLines.map(line => (
                  <tr key={line.id}>
                    <td>{line.employeeId}</td>
                    <td><StatusBadge status={line.approvalStatus}/></td>
                  </tr>
              ))
          )}
          </tbody>
        </table>

        {/* 문서 내용 */}
        <div className={styles.contentBox}>
          <div className={styles.sectionTitle}>문서 내용</div>
          <div>{approvalFile.text}</div>
        </div>

        {/* 첨부파일 목록 */}
        <div className={styles.fileSection}>
          <div className={styles.sectionTitle}>첨부파일</div>
          {approvalFile.files.length === 0 ? (
              <p>첨부파일 없음</p>
          ) : (
              approvalFile.files.map(file => (
                  <div className={styles.fileRow} key={file.approvalFileNo}>
                    <span>{file.originalFilename}</span>
                    <button className={styles.button}
                            onClick={() => handleDownload(file.approvalFileNo, file.originalFilename)}>다운로드
                    </button>
                  </div>
              ))
          )}
        </div>
      </div>
  );
};

export default FilePage;

const handleDownload = async (fileId: string, fileName: string) => {
  const response = await fetch(`http://127.0.0.1:1006/approval/file/download/${fileId}`);
  const blob = await response.blob();
  const link = document.createElement('a');
  link.href = window.URL.createObjectURL(blob);
  link.download = fileName;
  document.body.appendChild(link);
  link.click();
  link.remove();
};