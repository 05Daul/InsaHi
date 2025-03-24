import React from 'react';
import styles from '../../../../../../../무제 폴더 3/0319/insafront/src/styles/paystubsearch.module.css';

interface PaystubSearchProps {
    selectedYear: string;
    onYearChange: (year: string) => void;
    onSearch: () => void;
}

const PaystubSearch: React.FC<PaystubSearchProps> = ({
                                                         selectedYear,
                                                         onYearChange,
                                                         onSearch
                                                     }) => {
    return (
        <div className={styles.searchContainer}>
            <label htmlFor="yearSelect">급여명세서</label>
            <select
                id="yearSelect"
                value={selectedYear}
                onChange={(e) => onYearChange(e.target.value)}
            >
                <option value="2025">2025</option>
                <option value="2024">2024</option>
                <option value="2023">2023</option>
            </select>
            <button onClick={onSearch}>조회</button>
        </div>
    );
};

export default PaystubSearch;