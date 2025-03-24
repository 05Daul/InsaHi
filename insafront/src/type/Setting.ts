export interface AttendanceEntity {
    id: number;
    employeeId: string;
    companyCode: string;
    workDate: number; // 또는 string (예: 'YYYY-MM-DD')
    checkInTime: string; // 시간은 보통 string (예: '09:00')
    checkOutTime: string; // 시간은 보통 string (예: '18:00')
    workHours: number;
    overtimeHours: number;
    status: string; // 출근 상태 (예: '출근', '지각', '결근')
}

export interface Employee {
    employeeId: string;       // 직원 ID
    employeeName: string;     // 직원 이름
    departmentId: string;     // 부서 ID
    status: 'ACTIVE' | 'INACTIVE'; // 직원 상태
    positionName: string;     // 직급
}

export interface Department {
    departmentId: string;       // 부서 ID
    departmentName: string;     // 부서명
    employees?: Employee[];     // 직원 배열
    subDepartments?: Department[];    // 하위 부서 배열
    action?: string;            // 작업 정보
}


export interface Company {
    CompanyName: string;       // 부서 ID
}

export interface DeductionType {
    deductionTypeId: number;
    typeName: string;
    description?: string;
}
export interface AllowanceTypeOption {
    value: string;
    label: string;
}

export const allowanceTypes: AllowanceTypeOption[] = [
    { value: "MEAL", label: "식대" },
    { value: "TRANSPORT", label: "통근비" },
    { value: "CHILD_CARE", label: "보육수당" },
    { value: "OVERTIME", label: "연장근로수당" },
    { value: "POSITION", label: "직급수당" },
    { value: "CAR_ALLOWANCE", label: "자가운전보조금" },
    { value: "DANGER", label: "위험수당" },
    { value: "FIELD_WORK", label: "현장 근무수당" },
    { value: "ETC", label: "기타" }
];

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
    positionId: number;
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
    //allowType: AllowanceType; // 관계형 필드
    allowType: string; // 관계형 필드
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