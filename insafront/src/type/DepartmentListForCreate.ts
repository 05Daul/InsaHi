// types/DepartmentListForCreate.ts


export interface DepartmentListForCreate {
  departmentId: string;
  departmentName: string;
  employees: EmployeeDataForCreateDTO[];
  subDepartments: DepartmentListForCreate[]; // 재귀 구조
  action: string; // 예: "CREATE" | "UPDATE" | "DELETE"
}

export interface EmployeeDataForCreateDTO {
  employeeId: string;
  employeeName: string;
  departmentId: string;
  status: string; // 예: "ACTIVE" 또는 "INACTIVE"
}

export interface ActionBasedOrganizationChartForCreateDTO {
  departmentId: string;
  departmentName: string;
  action: string; // "CREATE", "UPDATE", "DELETE"
}
