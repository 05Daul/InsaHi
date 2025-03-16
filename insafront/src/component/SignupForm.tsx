
import { useState } from "react";
import { useForm, SubmitHandler } from "react-hook-form";
import styles from "@/styles/SignupForm.module.css";

// ✅ 입력 데이터 타입 정의
interface SignupFormData {
    company: string;
    employees: number;
    name: string;
    phone: string;
    email: string;
}

export default function SignupForm() {
    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm<SignupFormData>(); // ✅ 타입 적용
    const [submittedData, setSubmittedData] = useState<SignupFormData | null>(null);

    // ✅ 타입 적용하여 오류 해결
    const onSubmit: SubmitHandler<SignupFormData> = (data) => {
        setSubmittedData(data);
        console.log("회원가입 데이터:", data);
    };

    return (
        <div className={styles.container}>
            <div className={styles.card}>
                <h2 className={styles.title}>회원가입</h2>

                {submittedData ? (
                    <p className={styles.success}>회원가입이 완료되었습니다!</p>
                ) : (
                    <form onSubmit={handleSubmit(onSubmit)} className={styles.form}>
                        <div className={styles.formGroup}>
                            <label>회사명</label>
                            <input
                                {...register("company", { required: "회사명을 입력하세요" })}
                                placeholder="회사명을 입력하세요"
                            />
                            {errors.company && <p className={styles.error}>{errors.company.message}</p>}
                        </div>

                        <div className={styles.formGroup}>
                            <label>직원수</label>
                            <input
                                type="number"
                                {...register("employees", { required: "직원수를 입력하세요", valueAsNumber: true })}
                                placeholder="직원수를 입력하세요"
                            />
                            {errors.employees && <p className={styles.error}>{errors.employees.message}</p>}
                        </div>

                        <div className={styles.formGroup}>
                            <label>이름</label>
                            <input
                                {...register("name", { required: "이름을 입력하세요" })}
                                placeholder="이름을 입력하세요"
                            />
                            {errors.name && <p className={styles.error}>{errors.name.message}</p>}
                        </div>

                        <div className={styles.formGroup}>
                            <label>연락처</label>
                            <input
                                type="tel"
                                {...register("phone", { required: "연락처를 입력하세요" })}
                                placeholder="연락처를 입력하세요"
                            />
                            {errors.phone && <p className={styles.error}>{errors.phone.message}</p>}
                        </div>

                        <div className={styles.formGroup}>
                            <label>이메일</label>
                            <input
                                type="email"
                                {...register("email", {
                                    required: "이메일을 입력하세요",
                                    pattern: { value: /\S+@\S+\.\S+/, message: "유효한 이메일을 입력하세요" }
                                })}
                                placeholder="이메일을 입력하세요"
                            />
                            {errors.email && <p className={styles.error}>{errors.email.message}</p>}
                        </div>

                        <button type="submit" className={styles.submitButton}>
                            회원가입
                        </button>
                    </form>
                )}
            </div>
        </div>
    );
}
