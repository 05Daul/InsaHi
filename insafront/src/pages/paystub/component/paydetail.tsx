import React from 'react';
import styles from '../../../styles/paydetail.module.css';

interface PaydetailList {
    지급항목합계: number;
    기본급: number;
    연장수당: number;
    야간수당: number;
    휴일수당: number;
    차량유지비: number;
    식비: number;
    공제항목합계: number;
    근로소득세: number;
    근로지방소득세: number;
    국민연금: number;
    건강보험: number;
    장기요양보험: number;
    고용보험: number;
    학자금상환액: number;
    미지급금여: number;
}

interface PayDetailProps {
    payslipDetail: PaydetailList | null;
}

const PayDetail: React.FC<PayDetailProps> = ({ payslipDetail }) => {
    if (!payslipDetail) return <div className={styles.placeholder}>급여명세를 선택해주세요.</div>;

    return (
        <div className={styles.detailContainer}>
            <table className={styles.table}>
                <tbody>
                <tr>
                    <th>지급항목 합계</th>
                    <td>{payslipDetail.지급항목합계.toLocaleString()} 원</td>
                    <th>공제항목 합계</th>
                    <td>{payslipDetail.공제항목합계.toLocaleString()} 원</td>
                </tr>
                <tr>
                    <th>기본급</th>
                    <td>{payslipDetail.기본급.toLocaleString()} 원</td>
                    <th>근로소득세</th>
                    <td>{payslipDetail.근로소득세.toLocaleString()} 원</td>
                </tr>
                <tr>
                    <th>연장수당</th>
                    <td>{payslipDetail.연장수당.toLocaleString()} 원</td>
                    <th>근로지방소득세</th>
                    <td>{payslipDetail.근로지방소득세.toLocaleString()} 원</td>
                </tr>
                <tr>
                    <th>야간수당</th>
                    <td>{payslipDetail.야간수당.toLocaleString()} 원</td>
                    <th>국민연금</th>
                    <td>{payslipDetail.국민연금.toLocaleString()} 원</td>
                </tr>
                <tr>
                    <th>휴일수당</th>
                    <td>{payslipDetail.휴일수당.toLocaleString()} 원</td>
                    <th>건강보험</th>
                    <td>{payslipDetail.건강보험.toLocaleString()} 원</td>
                </tr>
                <tr>
                    <th>차량유지비</th>
                    <td>{payslipDetail.차량유지비.toLocaleString()} 원</td>
                    <th>장기요양보험</th>
                    <td>{payslipDetail.장기요양보험.toLocaleString()} 원</td>
                </tr>
                <tr>
                    <th>식비</th>
                    <td>{payslipDetail.식비.toLocaleString()} 원</td>
                    <th>고용보험</th>
                    <td>{payslipDetail.고용보험.toLocaleString()} 원</td>
                </tr>
                <tr>
                    <th>공제 후 지급액</th>
                    <td colSpan={1}>{(payslipDetail.지급항목합계 - payslipDetail.공제항목합계).toLocaleString()} 원</td>
                    <th>학자금상환액</th>
                    <td>{payslipDetail.학자금상환액.toLocaleString()} 원</td>
                </tr>
                <tr>
                    <th colSpan={3}>미지급 급여</th>
                    <td>{payslipDetail.미지급금여.toLocaleString()} 원</td>
                </tr>
                </tbody>
            </table>
        </div>
    );
};

export default PayDetail;