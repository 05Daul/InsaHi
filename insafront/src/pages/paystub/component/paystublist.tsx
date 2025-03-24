import React from 'react';
import styles from '../../../../../../../무제 폴더 3/0319/insafront/src/styles/paystublist.module.css';

type PaystubItem = {
    month: string;
    paymentDate: string;
    totalPay: number;
    totalDeductions: number;
    netPay: number;
};

interface PayStubListProps {
    payslipList: PaystubItem[];
    onSelectMonth: (month: string) => void;
}

const PayStubList: React.FC<PayStubListProps> = ({ payslipList, onSelectMonth }) => {
    return (
        <div className={styles.listContainer}>
            <table className={styles.table}>
                <thead>
                <tr>
                    <th>급여월</th>
                    <th>급여지급일</th>
                    <th>지급합계</th>
                    <th>공제합계</th>
                    <th>실지급액</th>
                </tr>
                </thead>
                <tbody>
                {payslipList.map((item) => (
                    <tr
                        key={item.month}
                        onClick={() => onSelectMonth(item.month)}
                        className={styles.tableRow}
                    >
                        <td>{item.month}</td>
                        <td>{item.paymentDate}</td>
                        <td>{item.totalPay.toLocaleString()} 원</td>
                        <td>{item.totalDeductions.toLocaleString()} 원</td>
                        <td>{item.netPay.toLocaleString()} 원</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default PayStubList;