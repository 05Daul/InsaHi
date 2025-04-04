import {attendanceFetcher} from '@/api/serviceFetcher/attendanceFetcher';
import {PositionSalaryStepEntity} from '@/type/Setting';
import {API_BASE_URL_Employee} from "@/api/api_base_url";

// 직급/호봉 목록 조회
export const fetchSalarySteps = async (companyCode: string): Promise<PositionSalaryStepEntity[]> => {
  try {
    return await attendanceFetcher(`/salary-step-list?companyCode=${companyCode}`);

  } catch (error) {
    console.error('직급/호봉 목록 조회 실패:', error);
    throw error;
  }
};
export const updateEmployeeSalaryStep = async (employeeId: string, positionSalaryId: number) => {
  const url = `${API_BASE_URL_Employee}/update-salary-step?employeeId=${employeeId}&positionSalaryId=${positionSalaryId}`;
  return await fetch(url, {method: 'PUT'});
};
// 직급/호봉 추가
export const insertSalaryStep = async (stepData: PositionSalaryStepEntity, companyCode: string): Promise<PositionSalaryStepEntity> => {
  try {
    const payload = {
      ...stepData,
      companyCode: companyCode
    };
    return await attendanceFetcher(`/step-insert`, {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(payload)
    });
  } catch (error) {
    console.error('직급/호봉 추가 실패:', error);
    throw error;
  }
};

export const updateSalaryStep = async (stepData: PositionSalaryStepEntity, companyCode: string): Promise<void> => {
  try {
    const payload = {
      ...stepData,
      companyCode: companyCode
    };

    await attendanceFetcher(`/step-update`, {
      method: 'PUT',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(payload)
    });
  } catch (error) {
    console.error('직급/호봉 수정 실패:', error);
    throw error;
  }
};

export const deleteSalaryStep = async (stepData: PositionSalaryStepEntity, companyCode: string): Promise<void> => {
  try {
    const payload = {
      ...stepData,
      companyCode
    };

    await attendanceFetcher(`/step-delete`, {
      method: 'DELETE',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(payload)
    });
  } catch (error) {
    console.error('직급/호봉 삭제 실패:', error);
    throw error;
  }
};

