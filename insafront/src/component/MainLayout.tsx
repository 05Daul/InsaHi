import Topbar from './topbar/Topbar'
import {LayoutProps} from "@/type/mytype";
import styles from "@/styles/Layout.module.css";
import {useRouter} from "next/router";
import {useEffect, useState} from "react";
import Sidebar6 from "@/component/sidebar/Sidebar6";
import Sidebar5 from "@/component/sidebar/Sidebar5";
import Sidebar4 from "@/component/sidebar/Sidebar4";
import Sidebar2 from "@/component/sidebar/Sidebar2";
import Sidebar1 from "@/component/sidebar/Sidebar1";

function MainLayout({children}: LayoutProps) {

  const [activeSidebar, setActiveSidebar] = useState<string | null>(null);
  const router = useRouter();
  // const isfullWidthPage = ["/mypage", "/chat"].some(path => router.pathname.startsWith(path));
  useEffect(() => {
    if (
        router.pathname.startsWith("/mypage") ||
        router.pathname.startsWith("/chat")
    ) {
      setActiveSidebar(null); // 마이페이지나 채팅으로 이동하면 사이드바 닫기
    }
  }, [router.pathname]);
  return (
      <>
        <Topbar activeSidebar={activeSidebar} setActiveSidebar={setActiveSidebar}/>
        <div className={styles.sidebarcontainer}>
          {/* 사이드바 */}
          {/*/!* 조건부 Sidebar 렌더링 *!/*/}
          {activeSidebar === "sidebar1" && <Sidebar1/>}
          {activeSidebar === "sidebar2" && <Sidebar2/>}
          {activeSidebar === "sidebar4" && <Sidebar4/>}
          {activeSidebar === "sidebar5" && <Sidebar5/>}
          {activeSidebar === "sidebar6" && <Sidebar6/>}
        </div>
        <div className={
          // isfullWidthPage ? styles.fullwidth :
          activeSidebar ? styles.withSidebarOpen :
              styles.withSidebar}>
          {children}
        </div>

      </>
  )
}

export default MainLayout;