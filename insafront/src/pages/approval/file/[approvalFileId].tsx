import {useState, useEffect} from 'react';
import {useRouter} from 'next/router';
import styles from '@/styles/approval/ApprovalDetail.module.css';
import {ApprovalStatus} from "@/type/ApprovalStatus";
import StatusBadge from "@/component/approval/StatusBadge";

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
      const response = await fetch(`http://127.0.0.1:1005/approval/file/${approvalFileId}`);
      const data = await response.json();
      setApprovalFile(data);
    };

    fetchApprovalFileDetails();
  }, [approvalFileId]);

  if (!approvalFile) return <div>Loading...</div>;

  const referenceLines = approvalFile.approvalLines.filter(
      line => line.approvalOrder === 0 && line.employeeId
  );

  const approverLines = approvalFile.approvalLines.filter(
      line => line.approvalOrder !== 0 && line.employeeId
  );

  const handleApproval = async (lineId: string, status: ApprovalStatus) => {
    const payload = {lineId, approveOrNot: status, reason};
    const response = await fetch(`http://127.0.0.1:1005/approval/permit`, {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(payload)
    });
    if (response.ok) router.push('/approval/documents');
  };

  return (
      <div className={styles.approvalDetailPageContainer}>
        <h1 className={styles.approvalDetailPageTitle}>결재 문서 상세</h1>

        <div className={styles.approvalDetailCard}>
          <p>문서 제목: {approvalFile.name}</p>
          <p>문서 내용: {approvalFile.text}</p>
          <p>상신자: {approvalFile.employeeId}</p>
          <StatusBadge status={approvalFile.status}/>
        </div>

        <div className={styles.approvalDetailCard}>
          <h2>참조자 목록</h2>
          {referenceLines.length === 0 ? (
              <p>참조자가 없습니다.</p>
          ) : (
              referenceLines.map(line => (
                  <div key={line.id}>
                    <span>{line.employeeId}</span>
                    <StatusBadge status={line.approvalStatus}/>
                  </div>
              ))
          )}
        </div>

        <div className={styles.approvalDetailCard}>
          <h2>결재자 목록</h2>
          {approverLines.length === 0 ? (
              <p>결재자가 없습니다.</p>
          ) : (
              approverLines.map(line => {
                const isMyTurn =
                    line.employeeId === employeeIdToken &&
                    line.approvalStatus === ApprovalStatus.PENDING;
                const isFromMenuThree = menu === '3';

                return (
                    <div key={line.id}>
                      <span>결재 순서 {line.approvalOrder}: {line.employeeId}</span>

                      {isMyTurn && isFromMenuThree ? (
                          <>
                    <textarea
                        value={reason}
                        onChange={(e) => setReason(e.target.value)}
                        placeholder="사유 입력"
                    />
                            <button
                                onClick={() => handleApproval(line.id, ApprovalStatus.APPROVED)}>
                              승인
                            </button>
                            <button
                                onClick={() => handleApproval(line.id, ApprovalStatus.REJECTED)}>
                              반려
                            </button>
                          </>
                      ) : (
                          <StatusBadge status={line.approvalStatus}/>
                      )}
                    </div>
                );
              })
          )}
        </div>

        <div className={styles.approvalDetailCard}>
          <h2>첨부파일 목록</h2>
          {approvalFile.files.map(file => (
              <div key={file.approvalFileNo}>
                <span>{file.originalFilename}</span>
                <button
                    onClick={() =>
                        handleDownload(file.approvalFileNo, file.originalFilename)
                    }
                >
                  다운로드
                </button>
              </div>
          ))}
        </div>
      </div>
  );
};

export default FilePage;

const handleDownload = async (fileId: string, fileName: string) => {
  const response = await fetch(`http://127.0.0.1:1005/approval/file/download/${fileId}`);
  const blob = await response.blob();
  const link = document.createElement('a');
  link.href = window.URL.createObjectURL(blob);
  link.download = fileName;
  document.body.appendChild(link);
  link.click();
  link.remove();
};
