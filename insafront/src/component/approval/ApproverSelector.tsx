// component/Approver/ApproverSelector.tsx
import styles from '@/styles/approval/Submit.module.css';
import React from "react";

interface Props {
  approvers: string[];
  setApprovers: (values: string[]) => void;
  allUsers: { employeeId: string; name: string }[];
  approverErrors: string[];
  setApproverErrors: (errors: string[]) => void;
}

function ApproverSelector({
                            approvers,
                            setApprovers,
                            allUsers,
                            approverErrors,
                            setApproverErrors
                          }: Props) {
  function handleChange(index: number, value: string) {
    const updated = [...approvers];
    updated[index] = value;
    setApprovers(updated);

    const errors = [...approverErrors];
    errors[index] = value.trim() === '' ? '결재자 ID를 입력하세요.' : '';
    setApproverErrors(errors);
  }

  function addField() {
    setApprovers([...approvers, '']);
    setApproverErrors([...approverErrors, '']);
  }

  function removeField(index: number) {
    const updated = [...approvers];
    updated.splice(index, 1);
    setApprovers(updated);

    const errors = [...approverErrors];
    errors.splice(index, 1);
    setApproverErrors(errors);
  }

  return (
      <div className={styles.submitFormGroup}>
        <label className={styles.submitLabel}>결재자</label>
        {approvers.map((value, index) => (
            <div key={index} className={styles.inputWithRemove}>
              <select
                  className={styles.submitInput}
                  value={value}
                  onChange={(e) => handleChange(index, e.target.value)}
              >
                <option value="">-- 결재자 선택 --</option>
                {allUsers.map(user => (
                    <option key={user.employeeId} value={user.employeeId}>
                      {user.name} ({user.employeeId})
                    </option>
                ))}
              </select>
              {approvers.length > 1 && (
                  <button type="button" onClick={() => removeField(index)}
                          className={styles.removeButton}>🗑️</button>
              )}
              {approverErrors[index] && <p className={styles.errorText}>{approverErrors[index]}</p>}
            </div>
        ))}
        <button type="button" onClick={addField} className={styles.addButton}>+ 추가</button>
      </div>
  );
}

export default ApproverSelector;
