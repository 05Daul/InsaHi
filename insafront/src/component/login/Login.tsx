import {FormEvent, useState} from "react";
import styles from "@/styles/login/Login.module.css";
import { useRouter } from "next/router";
import {login} from "@/api/action";
import Link from "next/link";

export default function Login() {
    const [companyCode, setCompanyCode] = useState("");
    const [employeeId, setEmployeeId] = useState("");
    const [password, setPassword] = useState("");
    const router = useRouter();

    async function loginHandleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault(); // 기본 동작(페이지 새로고침) 방지
        // FormData 객체 생성 (HTML 폼에서 가져옴)
        const loginData = new FormData();
        loginData.append("companyCode", companyCode);
        loginData.append("employeeId", employeeId);
        loginData.append("password", password);

        console.log("최초 요청 데이터:", loginData);
        // 서버 액션 호출
        const token = await login(loginData);

        if (token) {
            console.log("✅ 로그인 성공! 받은 토큰:", token);
            localStorage.setItem("accessToken", token); // 토큰 저장
            localStorage.setItem("employeeId", employeeId);
            router.push("/mypage/MyPage");
            // window.location.href = "/mypage/MyPage";
            alert("로그인 성공! 🎉");
        } else {
            alert("로그인 실패 ❌");
        }
    }

    return (
        <div className={styles.container}>
            <form className={styles.form} onSubmit={loginHandleSubmit}>
                <h2 className={styles.title}>인사HI 로그인</h2>
                <p className={styles.subtitle}>사내 인사 시스템에 로그인하세요</p>
                <input
                    type="text"
                    className={styles.input}
                    placeholder="회사 코드"
                    value={companyCode}
                    onChange={(e) => setCompanyCode(e.target.value)}
                    required
                />
                <input
                    type="text"
                    className={styles.input}
                    placeholder="아이디"
                    value={employeeId}
                    onChange={(e) => setEmployeeId(e.target.value)}
                    required
                />
                <input
                    type="password"
                    className={styles.input}
                    placeholder="비밀번호"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
                <button type="submit" className={styles.button}>로그인</button>
                <p className={styles.footer}>
                    계정이 없으신가요?<br/>
                    <Link href="/SignupForm" className={styles.signupLink}>회원가입</Link>
                </p>
            </form>
        </div>
    );
}