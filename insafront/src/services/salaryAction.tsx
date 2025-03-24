import React, { useState, useEffect } from 'react';
import {
    fetchAllowances,
    submitAllowance,
    fetchPositions,
    submitPosition
} from './salaryService';

import {
    AllowanceEntity,
    AllowanceTypeOption,
    PositionEntity,
    PositionSalaryStepEntity
} from '@/type/Setting'; // ✅ 타입 가져오기




export const UseSalaryStepActions=(
companyCode: string
)=> {
    const [stepSalary,  setStepSalary] = useState<PositionSalaryStepEntity[]>([]);
    const [stepSalarys,  setStepSalaryes] = useState({
        positionSalaryStepId: '',
        positionId: '',
        salaryStepId: 0,
        baseSalary: 0,
        positionAllowance: 0,
        overtimeAllowance: 0,
        baseAnnualLeave: 0,
        companyCode: companyCode
    });

}



// 2. 수당
export const useAllowanceActions = (
    companyCode: string,
) => {
    const [allowances, setAllowances] = useState<AllowanceEntity[]>([]);
    const [allowance, setAllowance] = useState({
        allowType: '',
        allowSalary: 0,
        companyCode: companyCode
    });

    useEffect(() => {
        if (companyCode) {
            loadAllowances();
        }
    }, [companyCode]);

    const loadAllowances = async () => {
        try {
            const data = await fetchAllowances(companyCode);
            setAllowances(data);
        } catch (error) {
            console.error(error);
        }
    };

    const handleAllowanceChange = (
        e: React.ChangeEvent<HTMLSelectElement | HTMLInputElement>
    ) => {
        const { name, value } = e.target;
        setAllowance(prev => ({
            ...prev,
            [name]: name === 'allowSalary' ? Number(value) : value
        }));
    };

    const handleSubmitAllowance = async () => {
        try {
            await submitAllowance(allowance, companyCode);
            alert('수당이 추가되었습니다.');
            loadAllowances();
        } catch (error) {
            console.error(error);
            alert('오류가 발생했습니다.');
        }
    };

    return {
        allowances,
        allowance,
        handleAllowanceChange,
        handleSubmitAllowance
    };
};


// 1. 직급 - 완료. 2번 수정으로 인해 밑으로 수정
export const usePositionActions = (companyCode: string) => {
    const [positions, setPositions] = useState<PositionEntity[]>([]);
    const [newPosition, setNewPosition] = useState<{ positionName: string }>({
        positionName: ''
    });

    useEffect(() => {
        if (companyCode) {
            loadPositions();
        }
    }, [companyCode]);

    const loadPositions = async () => {
        try {
            const data = await fetchPositions(companyCode);
            console.log('불러온 직급 리스트:', data); // ✅ 확인용 로그
            setPositions(data);
        } catch (error) {
            console.error(error);
        }
    };

    const handlePositionChange = (
        e: React.ChangeEvent<HTMLInputElement>
    ) => {
        const { name, value } = e.target;
        setNewPosition(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmitPosition = async () => {
        if (!newPosition.positionName) {
            alert('직급명을 입력해주세요.');
            return;
        }

        try {
            await submitPosition(newPosition, companyCode);
            alert('직급이 추가되었습니다.');
            setNewPosition({ positionName: '' });
            loadPositions();
        } catch (error) {
            console.error(error);
            alert('직급 추가 실패');
        }
    };

    return {
        positions,
        newPosition,
        handlePositionChange,
        handleSubmitPosition
    };
};
