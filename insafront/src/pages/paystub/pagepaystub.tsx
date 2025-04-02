import React, {useEffect, useState} from 'react';
import {attendanceFetcher} from '@/api/serviceFetcher/attendanceFetcher'
import {AllowanceEntity, PaystubType} from '@/type/Setting';
import styles from '@/styles/atdsal/paydetail.module.css'

const allowanceTypeLabels: { [key: string]: string } = {
  MEAL: '식대',
  TRANSPORT: '교통비',
  BONUS: '보너스',
  ETC: '기타',
  OVERTIME: '연장근로수당',
  CAR_ALLOWANCE: '자가운전보조금',
  DANGER: '위험수당',
  FIELD_WORK: '현장 근무수당'
};

const deductionTypeLabels: { [key: string]: string } = {
  NATIONAL_PENSION: '국민연금',
  HEALTH_INSURANCE: '건강보험',
  LONG_TERM_CARE: '장기요양보험',
  EMPLOYMENT_INSURANCE: '고용보험',
  INDUSTRIAL_ACCIDENT: '산재보험'
};

const PagePaystub = () => {
  const [paystubList, setPaystubList] = useState<PaystubType[]>([]);

  const [allowanceList, setAllowanceList] = useState<AllowanceEntity[]>([]);
  const [filteredList, setFilteredList] = useState<PaystubType[]>([]);
  const [selectedPaystub, setSelectedPaystub] = useState<PaystubType | null>(null);
  const [companyCode, setCompanyCode] = useState('');
  const [employeeId, setEmployeeId] = useState('');
  const [year, setYear] = useState<number>(new Date().getFullYear());
  const [month, setMonth] = useState<number>(new Date().getMonth() + 1);

  const fetchPaystubs = async (empId: string) => {
    try {
      const response = await attendanceFetcher<PaystubType[]>(`/payStub-findall?employeeId=${empId}`);

      setPaystubList(response);
      filterPaystubs(response, year, month);
      console.log('✅ 급여 명세서 전체 조회 결과:', response);
    } catch (error) {
      console.error('급여 명세서 전체 조회 실패:', error);
    }
  };

  const fetchAllowList = async (companyCode: string) => {
    try {
      const response = await attendanceFetcher<AllowanceEntity[]>(`/allowance-list?companyCode=${companyCode}`);

      setAllowanceList(response);
      console.log('1) 수당 결과:', response);
    } catch (error) {
      console.error('수당 리스트 조회 실패:', error);
    }
  };

  const filterPaystubs = (list: PaystubType[], selectedYear: number, selectedMonth: number) => {
    const filtered = list.filter((paystub) => {
      const paymentDate = new Date(paystub.paymentDate);
      return (
          paymentDate.getFullYear() === selectedYear &&
          paymentDate.getMonth() + 1 === selectedMonth
      );
    });
    setFilteredList(filtered);
  };

  useEffect(() => {
    const storedCompanyCode = localStorage.getItem('companyCode');
    const storedEmployeeId = localStorage.getItem('employeeId');

    if (storedCompanyCode && storedEmployeeId) {
      setCompanyCode(storedCompanyCode);
      setEmployeeId(storedEmployeeId);
      fetchPaystubs(storedEmployeeId);
      fetchAllowList(storedCompanyCode); // ✅ 수당 리스트도 함께 불러오기
    } else {
      alert('회사 코드나 사원번호가 없습니다. 다시 로그인 해주세요.');
    }
  }, []);
  useEffect(() => {
    if (filteredList.length > 0) {
      setSelectedPaystub(filteredList[0]);
    } else {
      setSelectedPaystub(null);
    }
  }, [filteredList]);
  //
  // useEffect(() => {
  //     filterPaystubs(paystubList, year, month);
  // }, [year, month, paystubList]);

  const handleSearch = () => {
    filterPaystubs(paystubList, year, month);
  };

  const handleSelect = (paystub: PaystubType) => {

    setSelectedPaystub(paystub);
    console.log('⭐ 선택된 paystub 확인:', paystub); // ✅ 추가
  };

  return (
      <div className={styles.pageWrapper}>
        <h1>급여 명세서 조회</h1>

        <div className="filter">
          <select value={year} onChange={(e) => setYear(parseInt(e.target.value))}>
            {[2024, 2025].map((y) => (
                <option key={y} value={y}>{y}년</option>
            ))}
          </select>
          <select value={month} onChange={(e) => setMonth(parseInt(e.target.value))}>
            {[...Array(12)].map((_, idx) => (
                <option key={idx + 1} value={idx + 1}>{idx + 1}월</option>
            ))}
          </select>
          <button className={styles.button} onClick={handleSearch}>조회</button>
        </div>

        <div className="paystub-list">
          <div className={styles.tableWrapper}>
            <table className={styles.userTable}>
              <thead>
              <tr>
                <th>사원번호</th>
                <th>지급 총액</th>
                <th>공제 총액</th>
                <th>추가 수당</th>
                <th>실지급액</th>
                <th>상세보기</th>
              </tr>
              </thead>
              <tbody>
              {filteredList.length > 0 ? (
                  filteredList.map((paystub) => (
                      <tr key={paystub.payStubId}>
                        <td>{paystub.employeeId || 'N/A'}</td>
                        <td>{paystub.totalPayment?.toLocaleString() ?? 0} 원</td>
                        <td>{paystub.totalAllowances?.toLocaleString() ?? 0} 원</td>
                        <td>{paystub.totalDeductions?.toLocaleString() ?? 0} 원</td>
                        <td>{paystub.netPay?.toLocaleString() ?? 0} 원</td>
                        <td>
                          <button className={styles.button}
                                  onClick={() => handleSelect(paystub)}>상세
                          </button>
                        </td>
                      </tr>
                  ))
              ) : (
                  <tr>
                    <td colSpan={6}>조회된 명세서가 없습니다.</td>
                  </tr>
              )}
              </tbody>
            </table>
          </div>
        </div>

        {selectedPaystub && (
            <div className="paystub-detail">
              <div className={styles.tableWrapper}>
                {/* 기본급 */}
                <table className={styles.userTable}>
                  <thead>
                  <tr>
                    <th>항목</th>
                    <th>금액</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr>
                    <td>기본급</td>
                    <td>{selectedPaystub.baseSalary?.toLocaleString()} 원</td>
                  </tr>
                  </tbody>
                </table>

                {/* 수당 내역 */}
                <table className={styles.userTable}>
                  <thead>
                  <tr>
                    <th colSpan={2}>📌 수당 내역</th>
                  </tr>
                  </thead>
                  <tbody>
                  {Array.isArray(selectedPaystub.allowances) && selectedPaystub.allowances.length > 0 ? (
                      selectedPaystub.allowances.map((allowance) => {
                        const label = allowanceTypeLabels[allowance.allowType] || allowance.allowType;
                        return (
                            <tr key={allowance.id}>
                              <td>{label}</td>
                              <td>{Number(allowance.allowSalary).toLocaleString()} 원</td>
                            </tr>
                        );
                      })
                  ) : allowanceList.length > 0 ? (
                      allowanceList.map((allowance) => {
                        const label = allowanceTypeLabels[allowance.allowType] || allowance.allowType;
                        return (
                            <tr key={allowance.allowanceId}>
                              <td>{label}</td>
                              <td>{Number(allowance.allowSalary).toLocaleString()} 원</td>
                            </tr>
                        );
                      })
                  ) : (
                      <tr>
                        <td colSpan={2}>수당 내역이 없습니다.</td>
                      </tr>
                  )}
                  </tbody>
                </table>

                {/* 공제 내역 */}
                <table className={styles.userTable}>
                  <thead>
                  <tr>
                    <th colSpan={2}>📌 공제 내역</th>
                  </tr>
                  </thead>
                  <tbody>
                  {Array.isArray(selectedPaystub.deductions) && selectedPaystub.deductions.length > 0 ? (
                      selectedPaystub.deductions.map((deduction) => {
                        const label = deductionTypeLabels[deduction.deductionType] || deduction.deductionType;
                        return (
                            <tr key={deduction.id}>
                              <td>{label}</td>
                              <td>{Number(deduction.amount).toLocaleString()} 원</td>
                            </tr>
                        );
                      })
                  ) : (
                      <tr>
                        <td colSpan={2}>공제 내역이 없습니다.</td>
                      </tr>
                  )}
                  </tbody>
                </table>

                {/* 총액 요약 */}
                <table className={styles.userTable}>
                  <thead>
                  <tr>
                    <th>합계 항목</th>
                    <th>금액</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr>
                    <td>기본급</td>
                    <td>{selectedPaystub.baseSalary?.toLocaleString()} 원</td>
                  </tr>
                  <tr>
                    <td>총 수당액</td>
                    <td>{selectedPaystub.totalAllowances?.toLocaleString()} 원</td>
                  </tr>
                  <tr>
                    <td>합계</td>
                    <td>{selectedPaystub.totalPayment.toLocaleString()} 원</td>
                  </tr>
                  <tr>
                    <td>총공제액</td>
                    <td>{selectedPaystub.totalDeductions?.toLocaleString()} 원</td>
                  </tr>
                  <tr>
                    <td>실지급액</td>
                    <td>{selectedPaystub.netPay?.toLocaleString()} 원</td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
        )}
      </div>
  );
};

export default PagePaystub;