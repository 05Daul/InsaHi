import Topbar from './topbar/Topbar'
import {LayoutProps} from "@/type/mytype";
import styles from "@/styles/Layout.module.css";
import {useRouter} from "next/router";

function MainLayout({children}: LayoutProps) {


  const router = useRouter();
  const isfullWidthPage = ["/mypage/MyPage", "/chat"].some(path => router.pathname.startsWith(path));
  return (
      <>
        <Topbar/>
        <div className={isfullWidthPage ? styles.fullwidth : styles.withSidebar}>
          {children}
        </div>

      </>
  )
}

export default MainLayout;