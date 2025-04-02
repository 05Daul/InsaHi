// component/Approver/ReferencedSelector.tsx
import styles from '@/styles/approval/Submit.module.css';

interface Props {
  referencedIds: string[];
  setReferencedIds: (values: string[]) => void;
  allUsers: { employeeId: string; name: string }[];
  referencedErrors: string[];
  setReferencedErrors: (errors: string[]) => void;
}

function ReferencedSelector({
                              referencedIds,
                              setReferencedIds,
                              allUsers,
                              referencedErrors,
                              setReferencedErrors
                            }: Props) {
  function handleChange(index: number, value: string) {
    const updated = [...referencedIds];
    updated[index] = value;
    setReferencedIds(updated);

    const errors = [...referencedErrors];
    errors[index] = value.trim() === '' ? '참조자 ID를 입력하세요.' : '';
    setReferencedErrors(errors);
  }

  function addField() {
    setReferencedIds([...referencedIds, '']);
    setReferencedErrors([...referencedErrors, '']);
  }

  function removeField(index: number) {
    const updated = [...referencedIds];
    updated.splice(index, 1);
    setReferencedIds(updated);

    const errors = [...referencedErrors];
    errors.splice(index, 1);
    setReferencedErrors(errors);
  }

  return (
      <div className={styles.submitFormGroup}>
        <label className={styles.submitLabel}>참조자</label>
        {referencedIds.map((value, index) => (
            <div key={index} className={styles.inputWithRemove}>
              <select
                  className={styles.submitInput}
                  value={value}
                  onChange={(e) => handleChange(index, e.target.value)}
              >
                <option value="">-- 참조자 선택 --</option>
                {allUsers.map(user => (
                    <option key={user.employeeId} value={user.employeeId}>
                      {user.name} ({user.employeeId})
                    </option>
                ))}
              </select>
              {referencedIds.length > 1 && (
                  <button type="button" onClick={() => removeField(index)}
                          className={styles.removeButton}>🗑️</button>
              )}
              {referencedErrors[index] &&
                  <p className={styles.errorText}>{referencedErrors[index]}</p>}
            </div>
        ))}
        <button type="button" onClick={addField} className={styles.addButton}>+ 추가</button>
      </div>
  );
}

export default ReferencedSelector;
