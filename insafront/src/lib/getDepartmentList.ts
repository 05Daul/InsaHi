// lib/department/getDepartmentList.ts
export const getDepartmentList = async (companyCode: string) => {
    const response = await fetch(`http://localhost:1010/api/${companyCode}/department/list`);

    if (!response.ok) {
        throw new Error('외부 API 호출 실패');
    }

    const data = await response.json();
    console.log('Fetched departments:', data);

    const departments = data.map((item: any) => ({
        departmentId: item.departmentId,
        departmentName: item.departmentName,
        employees: item.employees?.map((emp: any) => ({
            employeeId: emp.employeeId,
            employeeName: emp.employeeName,
            positionName: emp.positionName,
            departmentId: emp.departmentId,
            status: emp.status,
        })) || [], // 직원 배열이 없으면 빈 배열로 대체
        subDepartments: item.subDepartments?.map((child: any) => ({
            departmentId: child.departmentId,
            departmentName: child.departmentName,
            employees: child.employees?.map((emp: any) => ({
                employeeId: emp.employeeId,
                employeeName: emp.employeeName,
                positionName: emp.positionName,
                departmentId: emp.departmentId,
                status: emp.status,
            })) || [],
            subDepartments: child.subDepartments || [],
        })) || [],
    }));

    return departments;
};
