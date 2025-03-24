export interface DeductionType {
    deductionTypeId: number;
    typeName: string;
    description?: string;
}

export interface AllowanceType {
    allowanceTypeId: number;
    typeName: string;
    description?: string;
}

export interface PositionEntity {
    positionId: number;
    positionName: string;
    companyCode: string;
}

export interface PositionSalaryStepEntity {
    positionSalaryStepId: number;
    position: string;
    salaryStepId: number;
    baseSalary: number;
    positionAllowance: number;
    overtimeAllowance: number;
    baseAnnualLeave: number;
    companyCode: string;
}

export interface SeverancePayEntity {
    severancePayId: number;
    companyCode: string;
    empCode: string;
    employeeId: string;
    startDate: number; // 날짜를 숫자로 처리하거나 ISO 포맷 문자열로 변경 가능
    retirementDate: number;
    severanceAmount: number;
}

export interface PayStubEntity {
    payStubId: number;
    paymentDate: number;
    baseSalary: number;
    totalAllowances: number;
    overtimePay: number;
    totalPayment: number;
    totalTaxFreeAllowances: number;
    totalDeductions: number;
    netPay: number;
    companyCode: string;
    employeeId: string;

    allowances: AllowanceEntity[];
    deductions: DeductionEntity[];
}

export interface AllowanceEntity {
    allowanceId: number;
    companyCode: string;
    allowType: AllowanceType; // 관계형 필드
    allowSalary: number;
    payStub: PayStubEntity; // 참조 필드
}

export interface DeductionEntity {
    deductionId: number;
    deductionType: DeductionType; // 관계형 필드
    deductionAmount: number;
    payStub: PayStubEntity; // 참조 필드
    companyCode: string;
}

export interface EmployeeAllowEntity {
    id: number;
    employeeId: string;
    allowance: AllowanceEntity;
}