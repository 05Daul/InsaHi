import React from 'react';
import EmployeeSearch from './component/EmployeeSearch';
import AllowanceManager from './component/AllowanceManager';
import styles from '../../styles/AllowanceManager.module.css';

const EmployeeAllowanceManagement: React.FC = () => {
    return (
            <div className={styles.container}>
                <EmployeeSearch />
                <AllowanceManager />
            </div>
    );
};

export default EmployeeAllowanceManagement;