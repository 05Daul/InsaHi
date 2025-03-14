import React, { useState } from "react";
import { NextPage } from "next";
import styles from "@/styles/mypage/EditProfile.module.css";

interface Employee {
    employeeId: string;
    password: string;
    name: string;
    email: string;
    phoneNumber: string;
    address: string;
    departmentId: string;
    teamId: string;
    state: string;
    companyCode: string;
}

const EditProfile: NextPage = () => {
    const [employee, setEmployee] = useState<Employee>({
        employeeId: "EMP1234", // 예시 데이터
        password: "1234",
        name: "홍길동",
        email: "hong@example.com",
        phoneNumber: "010-1234-5678",
        address: "서울시 강남구",
        departmentId: "D001",
        teamId: "T001",
        state: "근무중",
        companyCode: "C12345",
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setEmployee({ ...employee, [name]: value });
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        console.log("Updated Employee Data:", employee);
        alert("정보가 업데이트되었습니다.");
    };

    return (
        <div className={styles.container}>
            <h2 className={styles.title}>개인정보 수정</h2>
            <form onSubmit={handleSubmit} className={styles.form}>
                {/* 아이디 (수정 불가능) */}
                <div className={styles.formGroup}>
                    <label>아이디</label>
                    <input type="text" value={employee.employeeId} disabled />
                </div>

                {/* 회사 코드 (수정 불가능) */}
                <div className={styles.formGroup}>
                    <label>회사 코드</label>
                    <input type="text" value={employee.companyCode} disabled />
                </div>

                {/* 나머지 수정 가능 필드 */}
                {Object.keys(employee).map((key) => {
                    if (key === "employeeId" || key === "companyCode") return null;
                    return (
                        <div key={key} className={styles.formGroup}>
                            <label>{key}</label>
                            <input
                                type="text"
                                name={key}
                                value={employee[key as keyof Employee]}
                                onChange={handleChange}
                            />
                        </div>
                    );
                })}

                <button type="submit" className={styles.submitButton}>수정 완료</button>
            </form>
        </div>
    );
};

export default EditProfile;
