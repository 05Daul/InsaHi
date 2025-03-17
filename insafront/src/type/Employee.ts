// type/Employee.ts
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