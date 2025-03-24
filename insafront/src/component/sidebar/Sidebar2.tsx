// 전자결재
import Link from 'next/link';
import {FaRegEdit} from 'react-icons/fa';
import {FiSettings} from 'react-icons/fi';
import styles from '@/styles/Sidebar.module.css';

export default function Sidebar2() {
  return (
      <aside className={styles.sidesidebar} style={{marginTop: '50px'}}>
        <button className={styles.sideactionbutton}>결재 진행하기</button>

        <h3 className={styles.sidesidebartitle}>결재 현황</h3>

        <h4 className={`${styles.sidemenusection} ${styles.sideflex}`}>
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

        <h4 className={styles.sidemenusection}>결재 문서함</h4>
        <nav>
          <ul className={styles.sidemenu}>
            <li><Link href="#">- 임시 저장 문서</Link></li>
            <li><Link href="#">- 결재 대기 문서</Link></li>
            <li><Link href="#">- 결재 수신 문서</Link></li>
            <li><Link href="#">- 참조/열람 대기 문서</Link></li>
            <li><Link href="#">- 결재 예정 문서</Link></li>
          </ul>
        </nav>

        <h4 className={styles.sidemenusection}>개인 문서함</h4>
        <nav>
          <ul className={styles.sidemenu}>
            <li><Link href="#">- 기간 문서함</Link></li>
            <li><Link href="#">- 발송 문서함</Link></li>
            <li><Link href="#">- 수신 문서함</Link></li>
            <li><Link href="#">- 공문 문서함</Link></li>
          </ul>
        </nav>

        <h4 className={styles.sidemenusection}>부서 문서함</h4>
        <nav>
          <ul className={styles.sidemenu}>
            <li><Link href="#">- 기간 문서함</Link></li>
            <li><Link href="#">- 발송 문서함</Link></li>
            <li><Link href="#">- 수신 문서함</Link></li>
            <li><Link href="#">- 공문 문서함</Link></li>
          </ul>
        </nav>

        <h4 className={`${styles.sidemenusection} ${styles.sideflex}`}>
          서명관리 <FiSettings className={styles.sideicon}/>
        </h4>

        <div className={styles.sidesidebarborder}></div>
      </aside>
  );
}