import TopBar from './topbar/Topbar';
import {ReactNode} from 'react';
import styles from '@/styles/Layout.module.css';

interface LayoutProps {
  children: ReactNode;
}

export default function MainLayout({children}: LayoutProps) {
  return (
      <div className={styles.layoutWrapper}>
        <TopBar/>
        <div className={styles.mainContent}>
          {children}
        </div>
      </div>
  );
}