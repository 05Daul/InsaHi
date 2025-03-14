export interface Employee {
    employeeId: string;       // 직원 ID
    employeeName: string;     // 직원 이름
    position: string;         // 직급 (필요에 따라 수정)
    departmentId: string;     // 부서 ID
    status: 'Active' | 'Inactive'; // 직원 상태
}

export interface Department {
    departmentId: string;       // 부서 ID
    departmentName: string;     // 부서명
    parentDepartmentId: string | null;  // 상위 부서 ID
    employees?: Employee[];     // 직원 배열
    children?: Department[];    // 하위 부서 배열
    action?: string;            // 작업 정보
}
