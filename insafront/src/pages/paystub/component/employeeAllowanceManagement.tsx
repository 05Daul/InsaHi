import React from 'react';
import EmployeeSearch from './EmployeeSearch';
import AllowanceManager from './AllowanceManager';
import styles from '../../../../../../../04021800/styles/atdsal/AllowanceManager.module.css';

const EmployeeAllowanceManagement: React.FC = () => {
  return (
      <div className={styles.container}>
        <EmployeeSearch/>
        <AllowanceManager/>
      </div>
  );
};

export default EmployeeAllowanceManagement;