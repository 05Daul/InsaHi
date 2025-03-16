import React, { useState } from 'react';
import styles from '../../../styles/AllowanceManager.module.css'

const AllowanceManager: React.FC = () => {
    const [allowances, setAllowances] = useState([
        { code: 'P00', description: '기본급', status: '사용' },
        { code: 'P02', description: '식대', status: '사용' },
        { code: 'P04', description: '차량유지비', status: '사용' },
    ]);

    const handleAddAllowance = () => {
        // TODO: 수당 추가 기능 구현 예정
        console.log('수당 추가');
    };

    return (
        <div className={styles.rightPanel}>
            <h3>수당 관리</h3>
            <div className={styles.allowanceHeader}>
                <button onClick={handleAddAllowance}>추가</button>
            </div>
            <table className={styles.allowanceTable}>
                <thead>
                <tr>
                    <th>수당 이름</th>
                    <th>수당 금액</th>
                    <th>추가 여부</th>
                </tr>
                </thead>
                <tbody>
                {allowances.map((item, index) => (
                    <tr key={index}>
                        <td>{item.code}</td>
                        <td>{item.description}</td>
                        <td>{item.status === '사용' ? '✅' : '⬜️'}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default AllowanceManager;