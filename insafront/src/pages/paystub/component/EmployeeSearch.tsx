import React, { useState } from 'react';
import styles from '../../../styles/EmployeeSearch.module.css';
import EmployeeListPanel from './EmployeeListPanel'

const EmployeeSearch: React.FC = () => {
    const [searchKeyword, setSearchKeyword] = useState('');
    const [showList, setShowList] = useState(false);

    const handleSearch = () => {
        console.log('검색:', searchKeyword);
        setShowList(true); // 조회 클릭 시 리스트 패널 보여줌
    };

    return (
        <div className={styles.container}>
            <h3 className={styles.title}>직원 검색</h3>
            <div className={styles.searchBox}>
                <input
                    type="text"
                    placeholder="직원명 입력"
                    value={searchKeyword}
                    onChange={(e) => setSearchKeyword(e.target.value)}
                    className={styles.searchInput}
                />
                <button onClick={handleSearch} className={styles.searchButton}>조회</button>
            </div>

            {showList && <EmployeeListPanel />}
        </div>
    );
};

export default EmployeeSearch;