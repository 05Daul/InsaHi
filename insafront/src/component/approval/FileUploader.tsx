// component/Approver/FileUploader.tsx
import {ChangeEvent} from 'react';
import styles from '@/styles/approval/Submit.module.css';

interface Props {
  files: File[];
  setFiles: (files: File[]) => void;
}

function FileUploader({files, setFiles}: Props) {
  function handleFileChange(e: ChangeEvent<HTMLInputElement>) {
    if (e.target.files && e.target.files.length > 0) {
      const fileList = Array.from(e.target.files);
      setFiles(fileList);
    }
  }

  return (
      <div className={styles.submitFormGroup}>
        <label className={styles.submitLabel}>첨부파일</label>
        <input
            type="file"
            className={styles.submitInput}
            multiple
            onChange={handleFileChange}
        />
      </div>
  );
}

export default FileUploader;
