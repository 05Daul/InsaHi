//근태/급여
import Link from 'next/link';
import { FaRegEdit } from 'react-icons/fa';
import { FiSettings } from 'react-icons/fi';
import '../../styles/Sidebar1.css';

export default function Sidebar1() {
    return (
        <aside className="sidebar" style={{ marginTop: '50px' }}>
            <h3 className="sidebar-title">
                출퇴근 관리 <FaRegEdit className="icon" />
            </h3>
            <nav>
                <ul className="menu-under">
                    <li><Link href="#">- 출퇴근 기록 조회</Link></li>
                    <li><Link href="#">- 출퇴근 체크</Link></li>

                    <li className="menu-section">연차/휴가 신청</li>
                    <li><Link href="#">- 휴가 신청</Link></li>
                    <li><Link href="#">- 휴가 신청 현황 조회</Link></li>

                    <li className="menu-section">급여 내역 조회</li>
                    <li><Link href="#">- 월별 급여 내역</Link></li>
                    <li><Link href="#">- 급여 명세서 다운로드</Link></li>

                    <li className="menu-section">부서 문서함</li>
                    <li><Link href="#">- 기간 문서함</Link></li>
                    <li><Link href="#">- 발송 문서함</Link></li>

                    <li className="menu-section flex">
                        서명관리 <FiSettings className="icon" />
                    </li>
                </ul>
            </nav>
            <div className="sidebar-border"></div>
        </aside>
    );
}
