import {useEffect, useState} from 'react';
import {
  deleteSalaryStep,
  fetchSalarySteps,
  insertSalaryStep,
  updateEmployeeSalaryStep
} from '@/services/positionSalaryStepService';
import {PositionSalaryStepEntity} from '@/type/Setting';


export const usePositionSalaryStepActions = (companyCode: string) => {
  const [positionSalarySteps, setPositionSalarySteps] = useState<PositionSalaryStepEntity[]>([]);
  const [newPositionSalaryStep, setNewPositionSalaryStep] = useState<PositionSalaryStepEntity>({
    baseAnnualLeave: 0,
    overtimeAllowance: 0,
    positionAllowance: 0,
    positionSalaryId: 0,
    positionId: 0,
    salaryStepId: 0,
    baseSalary: 0,
    companyCode: companyCode
  });

  // 회사코드가 변경될 때마다 직급호봉을 다시 조회합니다.
  useEffect(() => {
    if (companyCode) {
      fetchSteps(companyCode);
    }
  }, [companyCode]);

  // 직급호봉 리스트 조회 함수
  const fetchSteps = async (code: string) => {
    try {
      const data = await fetchSalarySteps(code);  // 서비스 함수 호출
      setPositionSalarySteps(data);
      console.log(data)
    } catch (error) {
      console.error('직급호봉 조회 실패', error);
    }
  };

  const handleUpdateEmployeeSalaryStep = async (employeeId: string, positionSalaryId: number) => {
    try {
      await updateEmployeeSalaryStep(employeeId, positionSalaryId);
      alert('대표자의 직급 호봉이 성공적으로 업데이트되었습니다.');
    } catch (error) {
      console.error('대표자 직급 호봉 업데이트 실패', error);
      alert('업데이트에 실패했습니다.');
    }
  };

  // 신규 직급호봉 값 변경 핸들러
  const handlePositionSalaryStepChange = (
      e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const {name, value} = e.target;

    setNewPositionSalaryStep(prev => ({
      ...prev,
      [name]: value,
    }));
  };

  // 직급호봉 추가 함수
  const handleSubmitPositionSalaryStep = async () => {
    try {
      await insertSalaryStep(newPositionSalaryStep, companyCode);
      alert('직급호봉이 성공적으로 추가되었습니다.');

      // 입력폼 초기화
      setNewPositionSalaryStep({
        baseAnnualLeave: 0,
        overtimeAllowance: 0,
        positionAllowance: 0,
        positionSalaryId: 0,
        positionId: 0,
        salaryStepId: 0,
        baseSalary: 0,
        companyCode: companyCode
      });

      // 목록 새로고침
      fetchSteps(companyCode);
    } catch (error) {
      console.error('직급호봉 추가 실패', error);
    }
  };


  // 직급호봉 삭제 함수
  const handleDeletePositionSalaryStep = async (step: PositionSalaryStepEntity) => {
    try {
      await deleteSalaryStep(step, step.companyCode);  // 서비스 함수 호출
      alert('직급호봉이 삭제되었습니다.');

      // 목록 새로고침
      fetchSteps(companyCode);
    } catch (error) {
      console.error('직급호봉 삭제 실패', error);
    }
  };

  return {
    positionSalarySteps,
    newPositionSalaryStep,
    handlePositionSalaryStepChange,
    handleSubmitPositionSalaryStep,
    handleDeletePositionSalaryStep,
    handleUpdateEmployeeSalaryStep
  };
};