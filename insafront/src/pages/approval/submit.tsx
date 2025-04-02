// SubmitPage.tsx (최종 리팩토링)
import {useState, useEffect, ChangeEvent, FormEvent} from 'react';
import styles from '@/styles/approval/Submit.module.css';
import {fetchAllEmployees, submitApproval} from '@/services/approvalService';
import ApproverSelector from '@/component/approval/ApproverSelector';
import ReferencedSelector from '@/component/approval/ReferencedSelector';
import FileUploader from '@/component/approval/FileUploader';

interface FormData {
  id: string;
  name: string;
  text: string;
}

function SubmitPage() {
  const [formData, setFormData] = useState<FormData>({id: '', name: '', text: ''});
  const [companyCode, setCompanyCode] = useState<string>('');
  const [employeeId, setEmployeeId] = useState<string | null>(null);
  const [approvers, setApprovers] = useState<string[]>(['']);
  const [referencedIds, setReferencedIds] = useState<string[]>(['']);
  const [files, setFiles] = useState<File[]>([]);
  const [allUsers, setAllUsers] = useState<{ employeeId: string; name: string }[]>([]);
  const [approverErrors, setApproverErrors] = useState<string[]>(['']);
  const [referencedErrors, setReferencedErrors] = useState<string[]>(['']);

  useEffect(() => {
    const storedEmployeeId = localStorage.getItem('employeeId') || '';
    const storedCompanyCode = localStorage.getItem('companyCode') || '';
    setEmployeeId(storedEmployeeId);
    setCompanyCode(storedCompanyCode);

    fetchAllEmployees(storedCompanyCode)
    .then(setAllUsers)
    .catch(err => console.error('사용자 목록 불러오기 실패:', err));
  }, []);

  function handleInputChange(e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) {
    const {name, value} = e.target;
    setFormData(prev => ({...prev, [name]: value}));
  }

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    const isApproverValid = approvers.every(v => v.trim() !== '');
    const isReferencedValid = referencedIds.every(v => v.trim() !== '');

    if (!isApproverValid || !isReferencedValid) {
      setApproverErrors(approvers.map(v => (v.trim() === '' ? '결재자 ID를 입력하세요.' : '')));
      setReferencedErrors(referencedIds.map(v => (v.trim() === '' ? '참조자 ID를 입력하세요.' : '')));
      alert('결재자 및 참조자 ID를 모두 입력해주세요.');
      return;
    }

    const jsonData = {
      ...formData,
      companyCode,
      employeeId,
      approvers: approvers.filter(v => v.trim() !== ''),
      referencedIds: referencedIds.filter(v => v.trim() !== '')
    };

    try {
      const status = await submitApproval(jsonData, files);
      alert(status === 200 ? '성공!' : `실패! 상태코드: ${status}`);
    } catch (error) {
      console.error('문서 상신 오류:', error);
    }
  }

  return (
      <div className={styles.submitPageContainer}>
        <div className={styles.submitMainContent}>
          <h1 className={styles.submitPageTitle}>결재 문서 상신 (파일 포함)</h1>
          <form onSubmit={handleSubmit} className={styles.submitCard}>
            <div className={styles.submitFormGroup}>
              <label className={styles.submitLabel}>제목</label>
              <input
                  type="text"
                  name="name"
                  className={styles.submitInput}
                  value={formData.name}
                  onChange={handleInputChange}
                  required
              />
            </div>

            <div className={styles.submitFormGroup}>
              <label className={styles.submitLabel}>내용</label>
              <textarea
                  name="text"
                  className={styles.submitTextarea}
                  value={formData.text}
                  onChange={handleInputChange}
                  required
              />
            </div>

            <ApproverSelector
                approvers={approvers}
                setApprovers={setApprovers}
                allUsers={allUsers}
                approverErrors={approverErrors}
                setApproverErrors={setApproverErrors}
            />

            <ReferencedSelector
                referencedIds={referencedIds}
                setReferencedIds={setReferencedIds}
                allUsers={allUsers}
                referencedErrors={referencedErrors}
                setReferencedErrors={setReferencedErrors}
            />

            <FileUploader files={files} setFiles={setFiles}/>

            <div className={styles.submitButtonGroup}>
              <button type="submit" className={styles.submitButton}>문서 상신</button>
            </div>
          </form>
        </div>
      </div>
  );
}

export default SubmitPage;
