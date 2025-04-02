// type/leave.ts
export interface AnnualLeaveRequestDTO {
  annualLeaveUsageId: number;
  annualLeaveId: number;
  employeeId: string;
  companyCode: string;
  startDate: Date; // 백엔드에서 LocalDate 사용 중
  stopDate: Date;
  reason: string;
  leaveApprovalStatus: string;
}

export interface AnnualLeaveDTO {
  annualLeaveId: number;
  baseLeave: number;
  additionalLeave: number;
  totalGrantedLeave: number;
  remainingLeave: number;
  usedLeave: number;
  employeeId: string;
  companyCode: string;
}

export interface PageResult<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  number: number; // 현재 페이지
  size: number;
}

export interface PageResponseDTO<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  number: number; // 현재 페이지
  size: number;
}