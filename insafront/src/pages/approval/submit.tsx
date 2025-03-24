import {useState, ChangeEvent, FormEvent, useEffect} from 'react';
import styles from '@/styles/approval/Submit.module.css';

interface FormData {
  id: string;
  name: string;
  text: string;
  approvers: string;
  referencedIds: string;
}

const SubmitPage = () => {
  const [formData, setFormData] = useState<FormData>({
    id: '',
    name: '',
    text: '',
    approvers: '',
    referencedIds: ''
  });
  const [companyCode, setCompanyCode] = useState<string>('');
  const [employeeId, setEmployeeIdToken] = useState<string | null>(null);

  useEffect(() => {
    if (typeof window !== 'undefined') {
      const storedEmployeeId = localStorage.getItem('employeeId') || 'defaultId';
      const storedcompanyCode = localStorage.getItem('companyCode') || 'defaultId';
      setEmployeeIdToken(storedEmployeeId);
      setCompanyCode(storedcompanyCode);
    }
  }, []);

  const handleInputChange = (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const {name, value} = e.target;
    setFormData((prevData) => ({...prevData, [name]: value}));
  };

  const [files, setFiles] = useState<File[]>([]);

  const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      const fileList = Array.from(e.target.files);
      setFiles(fileList);
    }
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();

    const formPayload = new FormData();

    const jsonData = {
      id: formData.id,
      name: formData.name,
      text: formData.text,
      companyCode: companyCode,
      employeeId: employeeId,
      approvers: formData.approvers.split(',').map(item => item.trim()),
      referencedIds: formData.referencedIds.split(',').map(item => item.trim())
    };
    formPayload.append('jsonData', JSON.stringify(jsonData));

    files.forEach(file => {
      formPayload.append('files', file);
    });

    try {
      const response = await fetch('http://127.0.0.1:1005/approval/submit', {
        method: 'POST',
        body: formPayload
      });

      if (response.ok) {
        alert('성공!');
      } else {
        alert(`실패! 상태코드: ${response.status}`);
      }
    } catch (error) {
      console.error('에러:', error);
    }
  };

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

            <div className={styles.submitFormGroup}>
              <label className={styles.submitLabel}>결재자 (쉼표로 구분)</label>
              <input
                  type="text"
                  name="approvers"
                  className={styles.submitInput}
                  value={formData.approvers}
                  onChange={handleInputChange}
                  placeholder="예: user1,user2"
              />
            </div>

            <div className={styles.submitFormGroup}>
              <label className={styles.submitLabel}>참조자 (쉼표로 구분)</label>
              <input
                  type="text"
                  name="referencedIds"
                  className={styles.submitInput}
                  value={formData.referencedIds}
                  onChange={handleInputChange}
                  placeholder="예: ref1,ref2"
              />
            </div>

            <div className={styles.submitFormGroup}>
              <label className={styles.submitLabel}>첨부파일</label>
              <input
                  type="file"
                  className={styles.submitInput}
                  multiple
                  onChange={handleFileChange}
              />
            </div>

            <div className={styles.submitButtonGroup}>
              <button type="submit" className={styles.submitButton}>문서 상신</button>
            </div>
          </form>
        </div>
      </div>
  );
};

export default SubmitPage;
