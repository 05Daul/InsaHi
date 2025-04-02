import React, {FormEvent, useEffect, useState} from 'react';
import styles from '@/styles/admin/RegisterEmployee.module.css';
import {useForm} from "react-hook-form";
import {RegisterEmployeeDTO} from "@/type/registeremployee";
import {RegisterEmployeeAction} from "@/api/admin/registeremployeeaction";
import {DepartmentListForCreate} from "@/type/DepartmentListForCreate";
import {getParentDepartments} from "@/services/createDepartmentAction";
import SelectDepartment from "@/component/department/SelectDepartment";
import {usePositionActions} from "@/services/salaryAction";
import {usePositionSalaryStepActions} from "@/services/positionSalaryStepAction";
import {useRouter} from "next/router";


export default function RegisterEmployee() {

  const {
    register,
    setValue,
    reset,
    formState: {errors},
  } = useForm<RegisterEmployeeDTO>();
  const [submittedData, setSubmittedData] = useState<RegisterEmployeeDTO | null>(null);

  const router = useRouter();

  async function onRegisterHandleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    // FormData 객체 생성 (HTML 폼에서 가져옴)
    const formData = new FormData(event.currentTarget);
    console.log("최초 요청 데이터:", formData);

    // 빈 필드 찾기
    const requiredFields = ["name", "password", "email", "phoneNumber", "hireDate"];
    const missingFields = requiredFields.filter(
        (field) => !formData.get(field)
    );

    if (!selectedDepartment) missingFields.push("departmentId");
    if (!selectedPositionSalaryId) missingFields.push("positionSalaryId");

    if (missingFields.length > 0) {
      alert(`입력되지 않은 항목: ${missingFields.join(", ")}`);
      return; // 중단
    }

    // departmentId와 positionSalaryId를 수동으로 추가
    formData.append("departmentId", selectedDepartment);
    formData.append("positionSalaryId", String(selectedPositionSalaryId));

    // 서버 액션 호출
    const response = await RegisterEmployeeAction(formData);
    console.log("서버 응답:", response);
    alert("직원등록이 완료되었습니다!");
    console.log(response); //  응답 메시지 저장

    // ✅ 모든 필드 초기화
    reset(); // input 초기화
    setSelectedDepartment(''); // 부서 선택 초기화
    setSelectedPositionSalaryId(''); // 직급 선택 초기화
  };


  const [departments, setDepartments] = useState<DepartmentListForCreate[]>([]);
  const [selectedDepartment, setSelectedDepartment] = useState('');
  const [selectedPositionSalaryId, setSelectedPositionSalaryId] = useState('');

  useEffect(() => {
    const companyCode = localStorage.getItem("companyCode");
    if (companyCode) {
      getParentDepartments(companyCode) // ← 이게 flat 구조를 주고 있다고 가정
      .then(setDepartments)
      .catch(console.error);
      // getPositionList(companyCode).then(setPositions); // 너가 만드는 함수
    }
  }, []);

  const [companyCodeFromToken, setCompanyCodeFromToken] = useState<string>('');

  const {
    positions,
  } = usePositionActions(companyCodeFromToken);

  const {
    positionSalarySteps,

  } = usePositionSalaryStepActions(companyCodeFromToken);

  useEffect(() => {
    const storedCompanyCode = localStorage.getItem('companyCode');

    if (storedCompanyCode) {
      setCompanyCodeFromToken(storedCompanyCode);
    } else {
      alert('회사 코드가 없습니다. 다시 로그인 해주세요.');
    }
  }, []);

  return (
      <div className={styles.container}>
        <h2 className={styles.title}>👤 직원 등록</h2>

        {submittedData ? (
            <p> 등록이 완료되었습니다 !!!</p>
        ) : (
            <form className={styles.form} onSubmit={onRegisterHandleSubmit}>
              <div className={styles.field}>
                <label className={styles.label}>이름</label>
                <input
                    {...register("name", {required: "이름을 입력하세요"})}
                    placeholder="이름 입력"
                    className={styles.input}
                />
                {errors.name &&
                    <p className={styles.error}>{errors.name.message}</p>}
              </div>

              <div className={styles.field}>
                <label className={styles.label}>비밀번호</label>
                <input
                    {...register("password", {required: "비밀번호를 입력하세요"})}
                    placeholder="비밀번호 입력"
                    className={styles.input}
                />
                {errors.password &&
                    <p className={styles.error}>{errors.password.message}</p>}
              </div>

              <div className={styles.field}>
                <label className={styles.label}>이메일</label>
                <input
                    {...register("email", {required: "이메일을 입력하세요"})}
                    placeholder="이메일 입력"
                    className={styles.input}
                />
                {errors.email &&
                    <p className={styles.error}>{errors.email.message}</p>}
              </div>
              {/*부서명 입력*/}
              <div className={styles.field}>
                <label className={styles.label}>부서</label>

                <SelectDepartment
                    departments={departments}
                    selected={selectedDepartment}
                    onChange={(value) => {
                      setSelectedDepartment(value);
                      setValue("departmentId", value); // react-hook-form에 선택 값 저장
                    }}
                />
                {errors.departmentId && (
                    <p className={styles.error}>{errors.departmentId.message}</p>
                )}
              </div>
              {/*직급명 입력*/}
              <div className={styles.field}>
                <label className={styles.label}>직급</label>
                <select
                    value={selectedPositionSalaryId}
                    onChange={(e) => {
                      setSelectedPositionSalaryId(e.target.value);
                      // @ts-ignore
                      setValue("positionSalaryId", e.target.value); // FormData에 값 저장
                    }}
                    className={styles.input}
                >
                  <option value="">호봉 선택</option>
                  {positionSalarySteps.map((item) => (
                      <option key={item.positionSalaryId} value={item.positionSalaryId}>
                        {positions.find(pos => pos.positionId === item.positionId)?.positionName || item.positionId} - {item.salaryStepId}호봉
                      </option>
                  ))}
                </select>
                {errors.positionSalaryId && (
                    <p className={styles.error}>{errors.positionSalaryId.message}</p>
                )}
              </div>

              <div className={styles.field}>
                <label className={styles.label}>연락처</label>
                <input
                    {...register("phoneNumber", {required: "연락처을 입력하세요"})}
                    placeholder="전화번호 입력"
                    className={styles.input}
                />
                {errors.phoneNumber &&
                    <p className={styles.error}>{errors.phoneNumber.message}</p>}
              </div>

              <div className={styles.field}>
                <label className={styles.label}>입사일</label>
                <input
                    {...register("hireDate", {required: "입사일을 입력하세요"})}
                    placeholder="입사일 입력"
                    className={styles.input}
                />
                {errors.hireDate &&
                    <p className={styles.error}>{errors.hireDate.message}</p>}
              </div>

              <button type="submit" className={styles.button}>
                등록하기
              </button>
            </form>
        )}
      </div>
  );
};