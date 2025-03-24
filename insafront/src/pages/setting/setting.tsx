import React, { useEffect, useState } from 'react';
import styles from '../../styles/setting.module.css'; // 기존 스타일은 유지
import { useAllowanceActions, usePositionActions } from '@/services/salaryAction';
import { usePositionSalaryStepActions } from '@/services/positionSalaryStepAction';
import { allowanceTypes } from '@/type/Setting';

const Setting: React.FC = () => {
    const [companyCodeFromToken, setCompanyCodeFromToken] = useState<string>('');

    useEffect(() => {
        const storedCompanyCode = localStorage.getItem('companyCode');
        const employeeId = localStorage.getItem('employeeId');

        if (storedCompanyCode) {
            setCompanyCodeFromToken(storedCompanyCode);
        } else {
            alert('회사 코드가 없습니다. 다시 로그인 해주세요.');
        }
    }, []);

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
                        {positions.map((position) => (
                            <tr key={position.positionId}>
                                <td>{position.positionName}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
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
                        {allowances.map((item) => (
                            <tr key={item.allowanceId}>
                                <td>
                                    {allowanceTypes.find(type => type.value === item.allowType)?.label || item.allowType}
                                </td>
                                <td>{Number(item.allowSalary).toLocaleString()}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </div>

            {/* 하단 영역 */}
            <div className={styles.bottomSection}>
                <h2 className={styles.title}>직급별 호봉 추가</h2>

                <div className={styles.inlineForm}>
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

                    <label>호봉 단계</label>
                    <input
                        type="number"
                        name="salaryStepId"
                        value={newPositionSalaryStep.salaryStepId}
                        onChange={handlePositionSalaryStepChange}
                    />

                    <label>호봉별 기본급</label>
                    <input
                        type="number"
                        name="baseSalary"
                        value={newPositionSalaryStep.baseSalary}
                        onChange={handlePositionSalaryStepChange}
                    />

                    <label>직급 수당</label>
                    <input
                        type="number"
                        name="positionAllowance"
                        value={newPositionSalaryStep.positionAllowance}
                        onChange={handlePositionSalaryStepChange}
                    />

                    <label>연장 수당</label>
                    <input
                        type="number"
                        name="overtimeAllowance"
                        value={newPositionSalaryStep.overtimeAllowance}
                        onChange={handlePositionSalaryStepChange}
                    />

                    <label>기본 연차</label>
                    <input
                        type="number"
                        name="baseAnnualLeave"
                        value={newPositionSalaryStep.baseAnnualLeave}
                        onChange={handlePositionSalaryStepChange}
                    />

                    <button onClick={handleSubmitPositionSalaryStep} className={styles.buttonInline}>
                        호봉 추가
                    </button>
                </div>

                <h3>직급별 호봉 목록</h3>
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
                    {positionSalarySteps.map((item) => (
                        <tr key={item.positionSalaryStepId}>
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
            </div>
        </div>
    );
};

export default Setting;