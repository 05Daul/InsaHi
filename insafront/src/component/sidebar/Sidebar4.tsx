// 인사관리
import Link from 'next/link';
// import { FaRegEdit } from 'react-icons/fa';
// import { FiSettings } from 'react-icons/fi';
import '../../styles/Sidebar1.css';

export default function Sidebar4() {
    return (
        <aside className="sidebar" style={{ marginTop: '50px' }}>
            <h3 className="sidebar-title">
                사용자 관리
            </h3>
            <nav>
                <ul className="menu-under">
                    <li className="menu-section"><Link href="#">부서 관리</Link></li>
                    <li className="menu-section"><Link href="#">직무/직급 관리</Link></li>
                    <li className="menu-section"><Link href="#">권한 관리</Link></li>
                </ul>
            </nav>
            <div className="sidebar-border"></div>
        </aside>
    );
}
