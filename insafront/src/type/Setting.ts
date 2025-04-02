export const allowanceTypes: AllowanceTypeOption[] = [
  {value: "MEAL", label: "식대"},
  {value: "TRANSPORT", label: "통근비"},
  {value: "OVERTIME", label: "연장근로수당"},
  {value: "CAR_ALLOWANCE", label: "자가운전보조금"},
  {value: "DANGER", label: "위험수당"},
  {value: "FIELD_WORK", label: "현장 근무수당"},
  {value: "ETC", label: "기타"}
];


export interface DeductionType {
  deductionType: string;
  amount: number;
}

export interface AllowanceTypeOption {
  value: string;
  label: string;
}


export interface AllowanceTypes {
  id: number;
  type: string;
  amount: number;
}

export interface PositionEntity {
  positionId: number;
  positionName: string;
  companyCode: string;
}

export interface PositionSalaryStepEntity {
  positionSalaryId: number;
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

export interface allowanceTypes {
  id: number;
  type: string;
  amount: number;
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

export interface Allowance {
  id: number;
  allowType: string;
  allowSalary: number;
}

export interface Deduction {
  id: number;
  deductionType: string;
  amount: number;
}

export interface PaystubType {
  paymentDate: number;
  payStubId: number;
  employeeId: number;
  employeeName: string;
  baseSalary: number;
  allowances: Allowance[];
  deductions: Deduction[];
  totalAllowances: number;
  totalDeductions: number;
  netPay: number;
  totalPayment: number;
}

