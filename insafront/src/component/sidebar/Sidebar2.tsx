// 전자결재
import Link from 'next/link';
import {FaRegEdit} from 'react-icons/fa';
import {FiSettings} from 'react-icons/fi';
import styles from '@/styles/Sidebar.module.css';

export default function Sidebar2() {
  return (
      <aside className={styles.sidesidebar} style={{marginTop: '50px'}}>

        <h4 className={`${styles.sidesidebartitle} ${styles.sideflex}`}>
          전자결재
        </h4>
        <nav>
          <ul className={`${styles.sidemenu} `}>
            <li className={`${styles.sideflex}`}>
              <Link href="/approval/submit">- 문서 상신하기</Link><FaRegEdit className={styles.sideicon}/>
            </li>
            <li><Link href="/approval/documents">- 문서함</Link></li>
          </ul>
        </nav>

        <div className={styles.sidesidebarborder}></div>
      </aside>
  );
}