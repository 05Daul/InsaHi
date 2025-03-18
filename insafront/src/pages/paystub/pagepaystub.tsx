import React, { useState } from 'react';
import PaystubSearch from './component/paystubsearch';
import PaySummary from './component/paysummary';
import PayStubList from './component/paystublist';
import PayDetail from './component/paydetail';
import styles from '../../styles/pagepaystub.module.css';



type PayDetailType = {
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
};

type PaystubListItem = {
    month: string;
    paymentDate: string;
    totalPay: number;
    totalDeductions: number;
    netPay: number;
};

const PagePayStub: React.FC = () => {
    const [selectedYear, setSelectedYear] = useState<string>('2025');
    const [paystubList, setPaystubList] = useState<PaystubListItem[]>([]);
    const [selectedMonth, setSelectedMonth] = useState<string>('');
    const [payDetail, setPayDetail] = useState<PayDetailType | null>(null);

    const handleSearch = () => {
        const dummyList: PaystubListItem[] = [
            { month: '2025-03', paymentDate: '2025-03-25', totalPay: 3500000, totalDeductions: 500000, netPay: 3000000 },
            { month: '2025-02', paymentDate: '2025-02-25', totalPay: 3400000, totalDeductions: 480000, netPay: 2920000 },
        ];
        setPaystubList(dummyList);
        setSelectedMonth('');
        setPayDetail(null);
    };

    const handleSelectMonth = (month: string) => {
        setSelectedMonth(month);

        const dummyDetail: PayDetailType = {
            지급항목합계: 3500000,
            기본급: 3000000,
            연장수당: 200000,
            야간수당: 100000,
            휴일수당: 200000,
            차량유지비: 0,
            식비: 0,
            공제항목합계: 500000,
            근로소득세: 150000,
            근로지방소득세: 50000,
            국민연금: 80000,
            건강보험: 70000,
            장기요양보험: 20000,
            고용보험: 30000,
            학자금상환액: 0,
            미지급금여: 0,
        };

        setPayDetail(dummyDetail);
    };

    return (

            <div className={styles.pageContainer}>
                <PaystubSearch
                    selectedYear={selectedYear}
                    onYearChange={setSelectedYear}
                    onSearch={handleSearch}
                />

                <div className={styles.summarySection}>
                    <PaySummary />
                </div>

                <div className={styles.mainContent}>
                    <div className={styles.payStubSection}>
                        <PayStubList
                            payslipList={paystubList}
                            onSelectMonth={handleSelectMonth}
                        />
                    </div>

                    <div className={styles.payDetailSection}>
                        <PayDetail payslipDetail={payDetail} />
                    </div>
                </div>
            </div>
    );
};

export default PagePayStub;