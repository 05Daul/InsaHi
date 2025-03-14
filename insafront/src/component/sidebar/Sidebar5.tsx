//조직도,주소록
import Link from 'next/link';
import {FaRegEdit} from 'react-icons/fa';
import {FiSettings} from 'react-icons/fi';
import styles from '@/styles/Sidebar.module.css';


export default function Sidebar5() {
    return (
        <aside className={styles.sidesidebar} style={{ marginTop: '50px' }}>
            <h3 className={styles.sidesidebartitle}>조직도</h3>

            <h4 className={styles.sidemenusection}>사내 주소록</h4>
            <nav>
                <ul className={styles.sidemenu}>
                    <li><Link href="#">-  <span className="highlight">32</span></Link></li>
                    <li><Link href="#">- </Link></li>
                </ul>
            </nav>

            <h4 className={styles.sidemenusection}></h4>
            <nav>
                <ul className={styles.sidemenu}>
                    <li><Link href="#">- <span className="highlight">16</span></Link></li>
                    <li><Link href="#">- </Link></li>
                </ul>
            </nav>

            <h4 className={styles.sidemenusection}></h4>
            <nav>
                <ul className={styles.sidemenu}>
                    <li><Link href="#">-  <span className="highlight">12</span></Link></li>
                    <li><Link href="#">- </Link></li>
                    <li><Link href="#">- </Link></li>
                </ul>
            </nav>
        </aside>
    );
}
