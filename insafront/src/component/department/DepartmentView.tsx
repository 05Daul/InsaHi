// components/department/DepartmentView.tsx
import {Department} from '@/type/Employee';

interface Props {
    department: Department,
    onClick?: () => void
}

const DepartmentView = ({department, onClick}: Props) => {
    if (!department) {
        return <div>부서 정보가 없습니다.</div>;
    }

    return (
        <div className="department-view"
             style={{marginLeft: '20px', borderLeft: '1px solid #ccc', paddingLeft: '10px'}}>
            <h3>{department.departmentName}</h3>

            {/* 직원 목록 출력 */}
            {department.employees && department.employees.length > 0 ? (
                <ul>
                    {department.employees.map((employee) => (
                        <li key={employee.employeeId}>
                            {employee.employeeName}
                            {employee.positionName}
                            {employee.status}


                        </li>
                    ))}
                </ul>
            ) : (
                <div>직원이 없습니다.</div>
            )}

            {/* 하위 부서 출력 */}
            {department.subDepartments && department.subDepartments.length > 0 && (
                <div className="children-departments">
                    {department.subDepartments.map((child) => (
                        <DepartmentView key={child.departmentId} department={child}/>
                    ))}
                </div>
            )}
        </div>
    );
};

export default DepartmentView;
