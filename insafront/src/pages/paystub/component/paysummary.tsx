import React from 'react';
import styles from '../../../styles/paysummary.module.css';

const PaySummary: React.FC = () => {
    return (
        <div className={styles.summaryContainer}>
            <h3>급여조회</h3>

            <table className={styles.summaryTable}>
                <tbody>
                <tr>
                    <th>급여유형</th>
                    <td>고정수당</td>
                    <th>연봉</th>
                    <td>36,000,000 원</td>
                    <th>월급</th>
                    <td>3,000,000 원</td>
                </tr>
                <tr>
                    <th>고정수당</th>
                    <td>0 원</td>
                    <th>고정수당 포함여부</th>
                    <td>Y</td>
                    <td></td>
                    <td></td>
                </tr>
                </tbody>
            </table>
        </div>
    );
};

export default PaySummary;