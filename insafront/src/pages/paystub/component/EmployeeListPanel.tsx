import React from 'react';
import styles from '../../../styles/EmployeeSearch.module.css'

const EmployeeListPanel: React.FC = () => {
    const employeeList = [
        { id: 'E001', name: '홍길동', department: '영업팀' },
        { id: 'E002', name: '김철수', department: '개발팀' },
        { id: 'E003', name: '이영희', department: '인사팀' },
    ];

    return (
        <div className={styles.listPanel}>
            <h4 className={styles.listTitle}>직원 리스트</h4>
            <ul className={styles.employeeList}>
                {employeeList.map((employee) => (
                    <li key={employee.id} className={styles.employeeItem}>
                        {employee.name} ({employee.department})
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default EmployeeListPanel;