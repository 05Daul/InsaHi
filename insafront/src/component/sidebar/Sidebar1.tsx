// 근태/급여
import Link from 'next/link';
import {FaRegEdit} from 'react-icons/fa';
import {FiSettings} from 'react-icons/fi';
import styles from '@/styles/Sidebar.module.css';

export default function Sidebar1() {
    return (
        <aside className={styles.sidesidebar} style={{marginTop: '50px'}}>
            <h3 className={styles.sidesidebartitle}>
                출퇴근 관리 <FaRegEdit className={styles.sideicon}/>
            </h3>
            <nav>
                <ul className={styles.sidemenu}>
                    <li>
                        <Link href="/attendance/attendancelog">- 출퇴근 기록 조회</Link>
                    </li>
                    <li>
                        <Link href="/attendance/attendancelog">- 출퇴근 체크</Link>
                    </li>

                    <li className={styles.sidemenusection}>연차/휴가 신청</li>
                    <li>
                        <Link href="#">- 휴가 신청</Link>
                    </li>
                    <li>
                        <Link href="#">- 휴가 신청 현황 조회</Link>
                    </li>

                    <li className={styles.sidemenusection}>급여 내역 조회</li>
                    <li>
                        <Link href="#">- 월별 급여 내역</Link>
                    </li>
                    <li>
                        <Link href="#">- 급여 명세서 다운로드</Link>
                    </li>

                    <li className={`${styles.sidemenusection} ${styles.sideflex}`}>
                        서명관리 <FiSettings className={styles.sideicon}/>
                    </li>
                </ul>
            </nav>
            <div className={styles.sidesidebarborder}></div>
        </aside>
    );
}
