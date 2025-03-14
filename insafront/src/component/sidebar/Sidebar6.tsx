// 인사관리
import Link from 'next/link';
import { FaRegEdit } from 'react-icons/fa';
import { FiSettings } from 'react-icons/fi';
import '../../styles/Sidebar1.module.css';
import styles from "@/styles/Sidebar.module.css";

export default function Sidebar6() {
    return (
        <aside className={styles.sidesidebar} style={{marginTop: '50px'}}>
            <h3 className={styles.sidesidebartitle}>
                사용자 관리
            </h3>
            <nav>
                <ul className={styles.sidemenu}>
                    <li className={styles.sidemenu}><Link href="#">부서 관리</Link></li>
                    <li className={styles.sidemenu}><Link href="#">직무/직급 관리</Link></li>
                    <li className={styles.sidemenu}><Link href="#">권한 관리</Link></li>
                </ul>
            </nav>
            <div className={styles.sidesidebarborder}></div>
        </aside>
    );
}
