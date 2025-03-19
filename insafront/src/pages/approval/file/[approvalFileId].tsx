import {useState, useEffect} from 'react';
import {useRouter} from 'next/router';

interface File {
  approvalFileNo: string;
  originalFilename: string;
  storeFilename: string;
}

interface ApprovalFile {
  id: string;
  name: string;
  text: string;
  companyCode: string;
  employeeId: string;
  status: string;  // 결재 문서 상태 (APPROVED, PENDING, REJECTED)
  files: File[];
}

const FilePage = () => {
  const router = useRouter();
  const {approvalFileId} = router.query;
  const [approvalFile, setApprovalFile] = useState<ApprovalFile | null>(null);

  useEffect(() => {
    // approvalFileId가 존재하는지 확인
    if (!approvalFileId) {
      console.log("approvalFileId is undefined or null");
      return;  // approvalFileId가 없으면 fetch를 호출하지 않음
    }

    const fetchApprovalFileDetails = async () => {
      try {
        const response = await fetch(`http://127.0.0.1:1005/approval/file/${approvalFileId}`);
        if (!response.ok) {
          throw new Error(`Failed to fetch, status code: ${response.status}`);
        }
        const data = await response.json();
        console.log("Fetched file data:", data);
        setApprovalFile(data);
      } catch (error) {
        console.error("Error fetching file details:", error);
      }
    };

    fetchApprovalFileDetails();
  }, [approvalFileId]);

  const handleDownload = async (fileId: string) => {
    if (!fileId) {
      console.error("fileId is undefined or null");
      return;
    }
    try {
      // 다운로드 URL에서 'fileId'를 제대로 전달
      const response = await fetch(`http://127.0.0.1:1005/approval/file/download/${fileId}`);
      const blob = await response.blob();
      const link = document.createElement('a');
      const url = window.URL.createObjectURL(blob);
      link.href = url;
      link.setAttribute('download', fileId);  // 다운로드될 파일명 (확장자명 포함시 파일명을 바꿀 수 있음)
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    } catch (error) {
      console.error("Error during file download:", error);
    }
  };

  if (!approvalFile) {
    return <div>Loading...</div>;
  }

  return (
      <div>
        <h1>결재 문서 상세</h1>
        <p>문서 제목: {approvalFile.name}</p>
        <p>문서 내용: {approvalFile.text}</p>
        <p>결재 문서 상신자: {approvalFile.employeeId}</p>
        <p>결재 상태: {approvalFile.status}</p> {/* 결재 상태 추가 */}
        <h2>첨부파일 목록</h2>
        {approvalFile.files.length === 0 ? (
            <p>첨부파일이 없습니다.</p>
        ) : (
            <ul>
              {approvalFile.files.map(file => (
                  <li key={file.approvalFileNo}>
                    <span>{file.originalFilename}</span>
                    <span>{file.approvalFileNo}</span>
                    <button onClick={() => handleDownload(file.approvalFileNo)}>다운로드</button>
                  </li>
              ))}
            </ul>
        )}
      </div>
  );
};

export default FilePage;
