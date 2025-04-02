import React, {useEffect, useState} from "react";
import Link from "next/link";
import {useRouter} from "next/router";

import styles from "@/styles/Topbar.module.css";
import EmployeeInfoAction from "@/api/mypage/employeeinfoaction";
import useSSE from "@/component/approval/useSSE";
import Toast from "@/component/approval/Toast";

type TopBarProps = {
  activeSidebar: string | null;
  setActiveSidebar: (sidebar: string | null) => void;
};

// JWT 디코딩 함수
function decodeJWT(token: string) {
  try {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
        atob(base64)
        .split('')
        .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    );
    return JSON.parse(jsonPayload);
  } catch (error) {
    console.error("토큰 디코딩 실패:", error);
    return null;
  }
}

const TopBar = ({activeSidebar, setActiveSidebar}: TopBarProps) => {
  const [hasNotification, setHasNotification] = useState(false);
  const [toastMessage, setToastMessage] = useState<string | null>(null);
  const [employee, setEmployee] = useState<employeeInfoDTO | null>(null);
  const [employeeIdToken, setEmployeeIdToken] = useState<string | null>(null);
  const [isAdmin, setIsAdmin] = useState(false);

  const router = useRouter();

  const employeeId =
      typeof window !== "undefined" ? localStorage.getItem("employeeId") : null;

  // SSE 알림 수신
  useSSE(employeeId, async (message) => {
    console.log("📩 메시지 수신:", message);
    setToastMessage(message);

    // 결재할 문서 존재 여부 API 호출
    const res = await fetch(`http://127.0.0.1:1006/approval/has-pending/${employeeId}`);
    const {hasPending} = await res.json();

    setHasNotification(hasPending); // 🔴 또는 ❌ 상태 갱신
    // 모든 페이지에서 이 이벤트로 반응할 수 있음
    window.dispatchEvent(new Event("sse-notify"));
  });
  const handleLogout = () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("employeeId");
    localStorage.removeItem("companyCode");
    localStorage.removeItem("auth");
    alert("로그아웃 되었습니다.");
    router.push("/");
  };

  useEffect(() => {
    if (typeof window !== "undefined") {
      const storedEmployeeId = localStorage.getItem("employeeId") || "defaultId";
      const storedAuthorityName = localStorage.getItem("authorityName")
      setEmployeeIdToken(storedEmployeeId);

      const token = localStorage.getItem("accessToken");
      if (token) {
        const decoded = decodeJWT(token);
        if (decoded && decoded.auth && decoded.auth.includes("ROLE_ADMIN")) {
          setIsAdmin(true);
        }
      }
    }
  }, []);

  useEffect(() => {
    if (employeeIdToken) {
      const fetchData = async () => {
        const data = await EmployeeInfoAction(employeeIdToken);
        if (data) {
          setEmployee(data);
        } else {
          console.warn("No data found.");
        }
      };
      fetchData();
    }
  }, [employeeIdToken]);
  useEffect(() => {
    const checkNotification = async () => {
      if (!employeeId) return;

      const res = await fetch(`http://127.0.0.1:1006/approval/has-pending/${employeeId}`);
      const {hasPending} = await res.json();

      setHasNotification(hasPending);
    };

    checkNotification();
  }, [employeeId]);

  return (
      <div className={styles.topcontainer}>
        <header className={styles.toptopbar}>
          <nav className={styles.topnav}>
            <Link href={"/mypage/MyPage"} className={styles.toplogo}>인사 HI</Link>

            <ul className={styles.topmenu}>
              <li>
                <Link href="/attendance/attendancelog" className={styles.topmenulink}
                      onClick={() => setActiveSidebar(activeSidebar === "sidebar1" ? null : "sidebar1")}>
                  근태/급여
                </Link>
              </li>
              <li className={styles.topmenuapproval}>
                <Link href="/approval/submit" className={styles.topmenulink}
                      onClick={() => setActiveSidebar(activeSidebar === "sidebar2" ? null : "sidebar2")}>
                  전자결재
                </Link>
                {hasNotification && <span className={styles.notificationDot}/>}
              </li>
              <li>
                <Link href="#" className={styles.topmenulink}
                      onClick={() => setActiveSidebar(activeSidebar === "sidebar3" ? null : "sidebar3")}>
                  주소록
                </Link>
              </li>
              <li>
                <Link href="#" className={styles.topmenulink}
                      onClick={() => setActiveSidebar(activeSidebar === "sidebar4" ? null : "sidebar4")}>
                  회의실
                </Link>
              </li>
              <li>
                <Link href="#" className={styles.topmenulink}
                      onClick={() => setActiveSidebar(activeSidebar === "sidebar5" ? null : "sidebar5")}>
                  게시판
                </Link>
              </li>
              {isAdmin && (

                  <li>
                    <Link href="/admin/RegisterEmployee" className={styles.topmenulink}
                          onClick={() => setActiveSidebar(activeSidebar === "sidebar6" ? null : "sidebar6")}>
                      인사관리
                    </Link>
                  </li>
              )}
              {isAdmin && (
                  <li>
                    <Link href="/setting/setting" className={styles.topmenulink}
                          onClick={() => setActiveSidebar(activeSidebar === "sidebar7" ? null : "sidebar7")}>
                      설정
                    </Link>
                  </li>
              )}
            </ul>

            <div className={styles.topicons}>
              <Link href="/chat" className={styles.topmenulink} style={{cursor: "pointer"}}>
                채팅
              </Link>
              <Link href={"/mypage/MyPage"} className={styles.user}>
                {employee?.name} 님
              </Link>
              <button className={styles.logoutButton} onClick={handleLogout}>
                로그아웃
              </button>
            </div>
          </nav>
        </header>

        {toastMessage && (
            <Toast message={toastMessage} onClose={() => setToastMessage(null)}/>
        )}
      </div>
  );
};

export default TopBar;