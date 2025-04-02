import React, {useEffect, useState} from 'react';
import styles from '../../styles/atdsal/setting.module.css'; // 기존 스타일은 유지
import {useAllowanceActions, usePositionActions} from '@/services/salaryAction';
import {usePositionSalaryStepActions} from '@/services/positionSalaryStepAction';
import {allowanceTypes} from '@/type/Setting';
import {fetcher} from "@/api/fetcher";

const Setting: React.FC = () => {
  const [companyCodeFromToken, setCompanyCodeFromToken] = useState<string>('');
  const [companyStartTime, setCompanyStartTime] = useState<string>('');
  const [newStartTime, setNewStartTime] = useState<string>('');
  const [selectedEmployeeId, setSelectedEmployeeId] = useState<string>('');
  const [selectedPositionSalaryId, setSelectedPositionSalaryId] = useState<string>('');
  const [positionPage, setPositionPage] = useState(1);
  const [allowancePage, setAllowancePage] = useState(1);
  const [salaryStepPage, setSalaryStepPage] = useState(1);
  const [selectedPositionFilter, setSelectedPositionFilter] = useState<string>('');

// 한 페이지에 보여줄 개수
  const ITEMS_PER_PAGE = 5;

// 페이징 처리 함수
  const paginate = (array: any[], page: number) => {
    const start = (page - 1) * ITEMS_PER_PAGE;
    return array.slice(start, start + ITEMS_PER_PAGE);
  };
  // ✅ 회사 시작 시간 조회 (GET)
  const handleGetStartTime = async () => {
    if (!companyCodeFromToken) {
      alert('회사 정보가 없습니다.');
      return;
    }

    const url = `http://127.0.0.1:1006/company/${companyCodeFromToken}/start-time`;
    console.log('조회 요청 URL:', url);

    try {
      const res = await fetcher<string>(url);
      if (res) {
        setCompanyStartTime(res);
        alert(`현재 회사 시작 시간은 ${res}입니다.`);
      }
    } catch (error) {
      console.error('회사 시작 시간 조회 실패', error);
      alert('회사 시작 시간 조회에 실패했습니다.');
    }
  };

  // ✅ 회사 시작 시간 등록 (POST)
  const handleAddStartTime = async () => {
    if (!companyCodeFromToken || !newStartTime) {
      alert('회사 코드와 시작 시간을 입력해주세요.');
      return;
    }

    const url = `http://127.0.0.1:1006/company/start-Time`;
    console.log('등록 요청 URL:', url);

    try {
      const res = await fetcher<string>(url, {
        method: 'POST',
        body: JSON.stringify({
          companyCode: companyCodeFromToken,
          startTime: newStartTime
        })
      });

      if (res) {
        alert(`회사 시작 시간이 ${newStartTime}으로 등록되었습니다.`);
        setCompanyStartTime(newStartTime);
        setNewStartTime('');
      }
    } catch (error) {
      console.error('회사 시작 시간 등록 실패', error);
      alert('회사 시작 시간 등록에 실패했습니다.');
    }
  };

  useEffect(() => {
    const storedCompanyCode = localStorage.getItem('companyCode');
    const employeeId = localStorage.getItem('employeeId');

    if (storedCompanyCode) {
      setCompanyCodeFromToken(storedCompanyCode);
    } else {
      alert('회사 코드가 없습니다. 다시 로그인 해주세요.');
    }

    if (employeeId) {
      setSelectedEmployeeId(employeeId);
    }
  }, []);

  const handleUpdateSalaryStep = async () => {
    if (!selectedEmployeeId || !selectedPositionSalaryId) {
      alert('직급/호봉을 입력해주세요.');
      return;
    }
    try {
      console.log(selectedPositionSalaryId)
      const url = `http://127.0.0.1:1006/employee/update-salary-step?employeeId=${selectedEmployeeId}&positionSalaryId=${Number(selectedPositionSalaryId)}`;
      console.log(url)
      await fetcher(url, {method: 'PUT'});
      alert('대표자의 직급 호봉이 성공적으로 업데이트되었습니다.');
    } catch (error) {
      console.error('업데이트 실패:', error);
      alert('대표자 직급 호봉 업데이트에 실패했습니다.');
    }
  };

  const {
    allowances,
    allowance,
    handleAllowanceChange,
    handleSubmitAllowance
  } = useAllowanceActions(companyCodeFromToken);

  const {
    positions,
    newPosition,
    handlePositionChange,
    handleSubmitPosition
  } = usePositionActions(companyCodeFromToken);

  const {
    positionSalarySteps,
    newPositionSalaryStep,
    handlePositionSalaryStepChange,
    handleSubmitPositionSalaryStep
  } = usePositionSalaryStepActions(companyCodeFromToken);

  const totalPositionPages = Math.ceil(positions.length / ITEMS_PER_PAGE);
  const totalAllowancePages = Math.ceil(allowances.length / ITEMS_PER_PAGE);
  const filteredSalarySteps = selectedPositionFilter
      ? positionSalarySteps.filter((step) => step.positionId === Number(selectedPositionFilter))
      : positionSalarySteps;
  const totalSalaryStepPages = Math.ceil(filteredSalarySteps.length / ITEMS_PER_PAGE);

  return (
      <div className={styles.pageWrapper}>
        {/* 상단 영역 */}
        <div className={styles.topSection}>

          {/* 직급 추가 */}
          <div className={styles.section}>
            <h2 className={styles.title}>직급 추가</h2>

            <div className={styles.inlineForm}>
              <label>직급 명</label>
              <input
                  type="text"
                  name="positionName"
                  value={newPosition.positionName}
                  onChange={handlePositionChange}
              />
              <button onClick={handleSubmitPosition} className={styles.buttonInline}>
                직급 추가
              </button>
            </div>

            <h3>회사 내 직급 목록</h3>
            <table className={styles.userTable}>
              <thead>
              <tr>
                <th>직급 명</th>
              </tr>
              </thead>
              <tbody>
              {paginate(positions, positionPage).map((position) => (
                  <tr key={position.positionId}>
                    <td>{position.positionName}</td>
                  </tr>
              ))}
              </tbody>
            </table>
            <div className={styles.pagination}>
              {Array.from({length: totalPositionPages}, (_, i) => (
                  <button
                      key={i + 1}
                      onClick={() => setPositionPage(i + 1)}
                      className={styles.button}
                      style={{
                        margin: '0 2px',
                        backgroundColor: positionPage === i + 1 ? '#ccc' : undefined
                      }}
                  >
                    {i + 1}
                  </button>
              ))}
            </div>
          </div>

          {/* 수당 추가 */}
          <div className={styles.section}>
            <h2 className={styles.title}>수당 추가</h2>

            <div className={styles.inlineForm}>
              <label>수당 종류</label>
              <select
                  name="allowType"
                  value={allowance.allowType}
                  onChange={handleAllowanceChange}
              >
                <option value="">수당 선택</option>
                {allowanceTypes.map(type => (
                    <option key={type.value} value={type.value}>
                      {type.label}
                    </option>
                ))}
              </select>

              <label>수당 금액</label>
              <input
                  type="text"
                  inputMode="numeric"
                  name="allowSalary"
                  placeholder="수당 금액"
                  value={allowance.allowSalary}
                  onChange={handleAllowanceChange}
              />

              <button onClick={handleSubmitAllowance} className={styles.buttonInline}>
                수당 추가
              </button>
            </div>

            <h3>회사 내 수당 목록</h3>
            <table className={styles.userTable}>
              <thead>
              <tr>
                <th>수당 종류</th>
                <th>수당 금액</th>
              </tr>
              </thead>
              <tbody>
              {paginate(allowances, allowancePage).map((item) => (
                  <tr key={item.allowanceId}>
                    <td>
                      {allowanceTypes.find(type => type.value === item.allowType)?.label || item.allowType}
                    </td>
                    <td>{Number(item.allowSalary).toLocaleString()}</td>
                  </tr>
              ))}
              </tbody>
            </table>
            <div className={styles.pagination}>
              {Array.from({length: totalAllowancePages}, (_, i) => (
                  <button
                      key={i + 1}
                      onClick={() => setAllowancePage(i + 1)}
                      className={styles.button}
                      style={{
                        margin: '0 2px',
                        backgroundColor: allowancePage === i + 1 ? '#ccc' : undefined
                      }}
                  >
                    {i + 1}
                  </button>
              ))}
            </div>
          </div>

          <div className={styles.section}>
            <h2 className={styles.title}>회사 시작 시간 관리</h2>

            {/* 조회 */}
            <div className={styles.inlineForm}>
              <label>회사 시작 시간 조회</label>
              <button
                  onClick={handleGetStartTime}
                  className={styles.buttonInline}
              >
                조회하기
              </button>
              {companyStartTime && (
                  <div style={{marginTop: '10px'}}>
                    <strong>현재 회사 시작 시간:</strong> {companyStartTime}
                  </div>
              )}
            </div>

            {/* 추가 */}
            <div className={styles.inlineForm} style={{marginTop: '20px'}}>
              <label>회사 시작 시간 입력</label>
              <input
                  type="time"
                  value={newStartTime}
                  onChange={(e) => setNewStartTime(e.target.value)}
              />
              <button
                  onClick={handleAddStartTime}
                  className={styles.buttonInline}
              >
                회사 시간 등록
              </button>
            </div>
          </div>
        </div>

        {/* 하단 영역 */}
        <div className={styles.bottomSection}>
          <h2 className={styles.title}>직급별 호봉 추가</h2>
          <div className={styles.bottomSection}>
            <h2 className={styles.title}>직급별 호봉 추가</h2>

            {/* 첫 번째 줄 */}
            <div className={styles.row}>
              <div className={styles.inputGroup}>
                <label>직급명</label>
                <select
                    name="positionId"
                    value={newPositionSalaryStep.positionId}
                    onChange={handlePositionSalaryStepChange}
                >
                  <option value="">직급 선택</option>
                  {positions.map((position) => (
                      <option key={position.positionId} value={position.positionId}>
                        {position.positionName}
                      </option>
                  ))}
                </select>
              </div>

              <div className={styles.inputGroup}>
                <label>호봉 단계</label>
                <input
                    type="number"
                    name="salaryStepId"
                    value={newPositionSalaryStep.salaryStepId}
                    onChange={handlePositionSalaryStepChange}
                />
              </div>

              <div className={styles.inputGroup}>
                <label>호봉별 기본급</label>
                <input
                    type="number"
                    name="baseSalary"
                    value={newPositionSalaryStep.baseSalary}
                    onChange={handlePositionSalaryStepChange}
                />
              </div>
            </div>

            {/* 두 번째 줄 */}
            <div className={styles.row}>
              <div className={styles.inputGroup}>
                <label>직급 수당</label>
                <input
                    type="number"
                    name="positionAllowance"
                    value={newPositionSalaryStep.positionAllowance}
                    onChange={handlePositionSalaryStepChange}
                />
              </div>

              <div className={styles.inputGroup}>
                <label>연장 수당</label>
                <input
                    type="number"
                    name="overtimeAllowance"
                    value={newPositionSalaryStep.overtimeAllowance}
                    onChange={handlePositionSalaryStepChange}
                />
              </div>

              <div className={styles.inputGroup}>
                <label>기본 연차</label>
                <input
                    type="number"
                    name="baseAnnualLeave"
                    value={newPositionSalaryStep.baseAnnualLeave}
                    onChange={handlePositionSalaryStepChange}
                />
              </div>
            </div>

            <button onClick={handleSubmitPositionSalaryStep} className={styles.buttonInline}>
              호봉 추가
            </button>
          </div>

          <div style={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            marginBottom: '10px'
          }}>
            <h3>직급별 호봉 목록</h3>
            <div className={styles.inlineForm}>
              <label>직급명 필터</label>
              <select
                  value={selectedPositionFilter}
                  onChange={(e) => setSelectedPositionFilter(e.target.value)}
              >
                <option value="">전체 보기</option>
                {positions.map((position) => (
                    <option key={position.positionId} value={position.positionId}>
                      {position.positionName}
                    </option>
                ))}
              </select>
            </div>
          </div>
          <table className={styles.userTable}>
            <thead>
            <tr>
              <th>직급명</th>
              <th>호봉</th>
              <th>호봉별 기본급</th>
              <th>직급 수당</th>
              <th>연장 수당</th>
              <th>기본 연차</th>
            </tr>
            </thead>
            <tbody>
            {paginate(filteredSalarySteps, salaryStepPage).map((item) => (
                <tr key={item.positionSalaryId}>
                  <td>
                    {positions.find(pos => pos.positionId === item.positionId)?.positionName || item.positionId}
                  </td>
                  <td>{item.salaryStepId}</td>
                  <td>{Number(item.baseSalary).toLocaleString()}</td>
                  <td>{Number(item.positionAllowance).toLocaleString()}</td>
                  <td>{Number(item.overtimeAllowance).toLocaleString()}</td>
                  <td>{item.baseAnnualLeave}</td>
                </tr>
            ))}
            </tbody>
          </table>
          <div className={styles.pagination}>
            {Array.from({length: totalSalaryStepPages}, (_, i) => (
                <button
                    key={i + 1}
                    onClick={() => setSalaryStepPage(i + 1)}
                    className={styles.button}
                    style={{
                      margin: '0 2px',
                      backgroundColor: salaryStepPage === i + 1 ? '#ccc' : undefined
                    }}
                >
                  {i + 1}
                </button>
            ))}
          </div>
        </div>
        {/* 대표자 직급 호봉 설정 */}
        <div className={styles.inlineForm} style={{marginTop: '30px'}}>
          <h3>대표자 직급 호봉 설정</h3>

          <label>직급 호봉 ID</label>
          <select
              value={selectedPositionSalaryId}
              onChange={(e) => setSelectedPositionSalaryId(e.target.value)}
          >
            <option value="">호봉 선택</option>
            {positionSalarySteps.map((item) => (
                <option
                    key={item.positionSalaryId}
                    value={item.positionSalaryId}
                >
                  {positions.find(pos => pos.positionId === item.positionId)?.positionName || item.positionId} - {item.salaryStepId}호봉
                </option>
            ))}
          </select>
          <button onClick={handleUpdateSalaryStep} className={styles.buttonInline}>
            대표자 직급 호봉 업데이트
          </button>
        </div>
      </div>
  );
};

export default Setting;