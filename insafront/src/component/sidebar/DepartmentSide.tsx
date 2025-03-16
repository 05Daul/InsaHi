//조직도,주소록
import Link from 'next/link';
import {FaRegEdit} from 'react-icons/fa';
import {FiSettings} from 'react-icons/fi';
import styles from '@/styles/Sidebar.module.css';


export default function DepartmentSide() {
    return (
        <aside className={styles.sidesidebar} style={{ marginTop: '50px' }}>
            <h3 className={styles.sidesidebartitle}>조직도</h3>

            <h4 className={styles.sidemenusection}>사내 주소록</h4>
            <nav>
                <ul className={styles.sidemenu}>
                    <li><Link href="#">- 전체 주소록 <span className="highlight">32</span></Link></li>
                    <li><Link href="#">- 부서별 연락처</Link></li>
                </ul>
            </nav>

            <h4 className={styles.sidemenusection}>개인 주소록</h4>
            <nav>
                <ul className={styles.sidemenu}>
                    <li><Link href="#">- 전체 주소록 <span className="highlight">16</span></Link></li>
                    <li><Link href="#">- 관광사 1팀</Link></li>
                </ul>
            </nav>

            <h4 className={styles.sidemenusection}>부서 주소록</h4>
            <nav>
                <ul className={styles.sidemenu}>
                    <li><Link href="#">- 전체 주소록 <span className="highlight">12</span></Link></li>
                    <li><Link href="#">- [디지털트윈] 관광사 공무 1팀</Link></li>
                    <li><Link href="#">- [지원금] 인공지능 2팀</Link></li>
                </ul>
            </nav>
        </aside>
    );
}
