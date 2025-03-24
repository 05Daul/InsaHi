import {attendanceFetcher} from '@/api/serviceFetcher/attendanceFetcher';
import {AllowanceEntity, PositionEntity,PositionSalaryStepEntity} from '@/type/Setting';


// 직급 호봉 추가, 조회
export const fetchSalaryStep = async (companyCode: string): Promise<PositionSalaryStepEntity[]> => {
    try {
        return await attendanceFetcher(`/salary-step-list`);
    } catch (error) {
        console.error('직급/호봉 목록 조회 실패:', error);
        throw error;
    }
};

export const submitSalaryStepTable = async (allowance: any,
                                            positionSalaryStepId: number,
position: string,
salaryStepId: number,
baseSalary: number,
positionAllowance: number,
overtimeAllowance: number,
baseAnnualLeave: number,
companyCode: string


) => {
    try {
        const payload = {
            ...allowance,
            companyCode: companyCode
        };

        return await attendanceFetcher('/allowance-insert', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(payload)
        });
    } catch (error) {
        console.error('수당 추가 실패:', error);
        throw error;
    }
};



// 1. 1) 수당 추가
export const submitSalaryStep = async (
    allowance: any,
    companyCode: string
) => {
    try {
        const payload = {
            ...allowance,
            companyCode: companyCode
        };

        return await attendanceFetcher('/allowance-insert', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(payload)
        });
    } catch (error) {
        console.error('수당 추가 실패:', error);
        throw error;
    }
};


// 1. 1) 수당 목록 조회
export const fetchAllowances = async (companyCode: string): Promise<AllowanceEntity[]> => {
    try {
        return await attendanceFetcher(`/allowance-list?companyCode=${companyCode}`);
    } catch (error) {
        console.error('수당 목록 조회 실패:', error);
        throw error;
    }
};

// 1. 1) 수당 추가
export const submitAllowance = async (
    allowance: any,
    companyCode: string
) => {
    try {
        const payload = {
            ...allowance,
            companyCode: companyCode
        };

        return await attendanceFetcher('/allowance-insert', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(payload)
        });
    } catch (error) {
        console.error('수당 추가 실패:', error);
        throw error;
    }
};

// ✅ 직급 목록 조회
export const fetchPositions = async (companyCode: string): Promise<PositionEntity[]> => {
    try {
        return await attendanceFetcher(`/position-list?companyCode=${companyCode}`);
    } catch (error) {
        console.error('직급 조회 실패:', error);
        throw error;
    }
};

// ✅ 직급 추가
export const submitPosition = async (
    position: any,
    companyCode: string
) => {
    try {
        const payload = {
            ...position,
            companyCode: companyCode
        };

        return await attendanceFetcher('/position-insert', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(payload)
        });
    } catch (error) {
        console.error('직급 추가 실패:', error);
        throw error;
    }
};