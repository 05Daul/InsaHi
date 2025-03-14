//전자결재
import Link from 'next/link';
import { FaRegEdit } from 'react-icons/fa';
import { FiSettings } from 'react-icons/fi';
import '../../styles/Sidebar2.css';

export default function Sidebar2() {
    return (
        <aside className="sidebar" style={{ marginTop: '50px' }}>
            <button className="action-button">결재 진행하기</button>
            <h3 className="sidebar-title">결재 현황</h3>

            <h4 className="menu-section">나의 문서함 <FaRegEdit className="icon" /></h4>
            <nav>
                <ul className="menu-under">
                    <li><Link href="#">- 필터링 1</Link></li>
                    <li><Link href="#">- 필터링 2</Link></li>
                </ul>
            </nav>

            <h4 className="menu-section">결재 문서함</h4>
            <nav>
                <ul className="menu-under">
                    <li><Link href="#">- 임시 저장 문서</Link></li>
                    <li><Link href="#">- 결재 대기 문서</Link></li>
                    <li><Link href="#">- 결재 수신 문서</Link></li>
                    <li><Link href="#">- 참조/열람 대기 문서</Link></li>
                    <li><Link href="#">- 결재 예정 문서</Link></li>
                </ul>
            </nav>

            <h4 className="menu-section">개인 문서함</h4>
            <nav>
                <ul className="menu-under">
                    <li><Link href="#">- 기간 문서함</Link></li>
                    <li><Link href="#">- 발송 문서함</Link></li>
                    <li><Link href="#">- 수신 문서함</Link></li>
                    <li><Link href="#">- 공문 문서함</Link></li>
                </ul>
            </nav>

            <h4 className="menu-section">부서 문서함</h4>
            <nav>
                <ul className="menu-under">
                    <li><Link href="#">- 기간 문서함</Link></li>
                    <li><Link href="#">- 발송 문서함</Link></li>
                    <li><Link href="#">- 수신 문서함</Link></li>
                    <li><Link href="#">- 공문 문서함</Link></li>
                </ul>
            </nav>

            <h4 className="menu-section flex">서명관리 <FiSettings className="icon" /></h4>
            <div className="sidebar-border"></div>
        </aside>
    );
}
