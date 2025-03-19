import Topbar from './topbar/Topbar'
import {LayoutProps} from "@/type/mytype";
import styles from "@/styles/Layout.module.css";

function MainLayout({children}:LayoutProps){
    return(
        <>
            <Topbar />
            <div className={styles.mainContent}>{children}</div>

        </>
    )
}

export default MainLayout;