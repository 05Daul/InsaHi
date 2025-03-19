
import {FormEvent, useState} from "react";
import { useForm } from "react-hook-form";
import styles from "@/styles/form/SignupForm.module.css";
import {Adminformtype, CompanyFormtype} from "@/type/signupformtype";
import {signup} from "@/api/action";
import {useRouter} from "next/router";


export default function SignupForm() {
    const {
        register,
        formState: { errors },
    } = useForm<CompanyFormtype & Adminformtype>();
    const [submittedData, setSubmittedData] = useState<CompanyFormtype & Adminformtype | null>(null);
    const router = useRouter();

    async function onSignupSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault(); // 기본 동작(새로고침) 방지

        // FormData 객체 생성 (HTML 폼에서 가져옴)
        const formData = new FormData(event.currentTarget);
        console.log("최초 요청 데이터:", formData);

        // 서버 액션 호출
        const response = await signup(formData);
        console.log("서버 응답:", response);
        alert("회원가입이 완료되었습니다!");

        // ✅ Next.js의 로그인 페이지로 이동
        // window.location.href = "/Login";

        console.log(response); //  응답 메시지 저장
        router.push("/");

    }

    async function onEmail(event: FormEvent<HTMLFormElement>){
        event.preventDefault();
    }



    return (
        <div className={styles.container}>
            <div className={styles.card}>
                <h2 className={styles.title}>회사 및 대표자 정보 입력</h2>

                {submittedData ? (
                    <p className={styles.success}>등록이 완료되었습니다!</p>
                ) : (
                    <form onSubmit={onSignupSubmit} className={styles.form}>

                        {/* 회사 정보 섹션 */}
                        <h3 className={styles.sectionTitle}>🏢 회사 정보 입력</h3>
                        <div className={styles.formGroup}>
                            <label>회사명</label>
                            <input {...register("companyName", {required: "회사명을 입력하세요"})} placeholder="회사명 입력"/>
                            {errors.companyName && <p className={styles.error}>{errors.companyName.message}</p>}
                        </div>

                        <div className={styles.formGroup}>
                            <label>직원 수</label>
                            <input {...register("headCount", {required: "직원 수를 입력하세요"})} placeholder="직원 수 입력"/>
                            {errors.headCount && <p className={styles.error}>{errors.headCount.message}</p>}
                        </div>

                        <div className={styles.formGroup}>
                            <label>회사 주소</label>
                            <input {...register("companyAddress", {required: "회사 주소를 입력하세요"})} placeholder="회사 주소 입력"/>
                            {errors.companyAddress && <p className={styles.error}>{errors.companyAddress.message}</p>}
                        </div>

                        <div className={styles.formGroup}>
                            <label>설립일</label>
                            <input {...register("createdAt", {required: "회사 설립일을 입력하세요"})} placeholder="회사 설립일 입력(년-월-일)"/>
                            {errors.createdAt && <p className={styles.error}>{errors.createdAt.message}</p>}
                        </div>

                        <div className={styles.formGroup}>
                            <label>사업자 번호</label>
                            <input {...register("businessNumber", {required: "사업자 번호를 입력하세요"})}
                                   placeholder="사업자 번호 입력"/>
                            {errors.businessNumber && <p className={styles.error}>{errors.businessNumber.message}</p>}
                        </div>

                        <hr className={styles.separator}/>

                        {/* 대표자 정보 섹션 */}
                        <h3 className={styles.sectionTitle}>👤 대표자 정보 입력</h3>
                        <div className={styles.formGroup}>
                            <label>대표자 이름</label>
                            <input {...register("name", {required: "이름을 입력하세요"})} placeholder="이름 입력"/>
                            {errors.name && <p className={styles.error}>{errors.name.message}</p>}
                        </div>
                        <div className={styles.formGroup}>
                            <label>비밀번호</label>
                            <input {...register("password", {required: "비밀번호를 입력하세요"})} placeholder="비밀번호 입력"/>
                            {errors.password && <p className={styles.error}>{errors.password.message}</p>}
                        </div>

                        <div className={styles.formGroup}>
                            <label>대표자 전화번호</label>
                            <input {...register("phone", {required: "전화번호를 입력하세요"})} placeholder="전화번호 입력"/>
                            {errors.phone && <p className={styles.error}>{errors.phone.message}</p>}
                        </div>

                        <div className={styles.formGroup}>
                            <label>주소</label>
                            <input type="address" {...register("address", {required: "주소를 입력하세요"})}
                                   placeholder="주소 입력"/>
                            {errors.address && <p className={styles.error}>{errors.address.message}</p>}
                        </div>

                        <div className={styles.formGroup}>
                            <label>대표자 이메일</label>
                            {/*<input type="email" {...register("email", {required: "이메일을 입력하세요"})} placeholder="이메일 입력"/>*/}
                            {/*{errors.email && <p className={styles.error}>{errors.email.message}</p>}*/}
                            {/*<button type="button" className={styles.sendButton}>발송</button>*/}
                            <div className={styles.emailContainer}>
                                <input type="email" {...register("email", { required: "이메일을 입력하세요" })} placeholder="이메일 입력" className={styles.emailInput} />
                                <button type="button" className={styles.sendButton}>인증</button>
                            </div>
                            {errors.email && <p className={styles.error}>{errors.email.message}</p>}

                        </div>


                        <button type="submit" className={styles.submitButton}>등록</button>
                    </form>
                )}
            </div>
        </div>
    );
}
