// pages/[companyCode]/department/list.tsx

import { GetServerSideProps } from 'next';
import { Employee, Department } from '../../../type/Employee'; // 타입 임포트

interface Props {
    departments: Department[]; // 부서 데이터
    additionalData: any; // 추가 데이터 (예: 예산, 프로젝트 등)
    error?: string; // 에러 메시지
}

// 트리 구조 렌더링 함수
const renderDepartment = (dept: Department, additionalData: any) => {
    console.log('Rendering department:', dept); // 각 부서 데이터 확인

    return (
        <div key={dept.departmentId} style={{ marginLeft: dept.parentDepartmentId ? 20 : 0 }}>
            <h3>{dept.departmentName}</h3>

            {/* 직원 목록 렌더링 */}
            {dept.employees && dept.employees.length > 0 ? (
                <ul>
                    {dept.employees.map((emp) => (
                        <li key={emp.employeeId}>
                            {emp.employeeName} - {emp.position} -
                            <span style={{ color: emp.status === 'Active' ? 'green' : 'red' }}>
                                {emp.status} {/* 직원 상태 표시 */}
                            </span>
                        </li>
                    ))}
                </ul>
            ) : (
                <p>No employees in this department</p> // 직원이 없을 경우 메시지 출력
            )}

            {/* 추가 데이터 (예: 부서 예산 등) */}
            {additionalData[dept.departmentId] && (
                <div>
                    <p>Budget: {additionalData[dept.departmentId].budget}</p>
                    <p>Active Projects: {additionalData[dept.departmentId].projects.length}</p>
                </div>
            )}

            {/* 하위 부서 트리 렌더링 (children이 있을 때만 렌더링) */}
            {dept.children && dept.children.length > 0 ? (
                <div>
                    {dept.children.map((child) => renderDepartment(child, additionalData))}
                </div>
            ) : (
                <p>No sub-departments</p> // 하위 부서가 없을 경우 메시지 출력
            )}
        </div>
    );
};

// 서버 사이드에서 데이터 받아오기
export const getServerSideProps: GetServerSideProps = async (context) => {
    const { companyCode } = context.query;
    console.log('Fetching data for company:', companyCode); // 회사 코드 확인

    try {
        // 부서 목록 가져오기
        const res = await fetch(`http://localhost:2025/api/${companyCode}/department/list`);
        const data = await res.json();
        console.log('Departments data:', data); // 부서 데이터 확인

        // 추가 데이터 (예: 예산, 프로젝트 등) 가져오기
        const additionalDataRes = await fetch(`http://localhost:2025/api/${companyCode}/additional-data`);
        const additionalData = await additionalDataRes.json();
        console.log('Additional data:', additionalData); // 추가 데이터 확인

        // 데이터를 부서와 연결하기
        const additionalDataMap = additionalData.reduce((acc: any, item: any) => {
            acc[item.departmentId] = item;
            return acc;
        }, {});

        const departments: Department[] = data.map((item: any) => {
            console.log('Department item:', item); // 각 부서 항목 확인
            return {
                departmentId: item["부서ID"],
                departmentName: item["부서명"],
                parentDepartmentId: item["상위부서ID"],
                employees: item["직원"].map((emp: any) => ({
                    employeeId: emp["직원ID"],
                    employeeName: emp["employeeName"],
                    position: emp["직급"] || "",
                    departmentId: emp["부서ID"],
                    status: emp["status"],
                })),
                children: item["하위부서"] ? item["하위부서"].map((child: any) => ({
                    departmentId: child["부서ID"],
                    departmentName: child["부서명"],
                    parentDepartmentId: item["부서ID"],
                    employees: child["직원"].map((emp: any) => ({
                        employeeId: emp["직원ID"],
                        employeeName: emp["직원이름"],
                        position: emp["직급"] || "",
                        departmentId: emp["부서ID"],
                        status: emp["status"],
                    })),
                    children: [], // 하위 부서가 있을 경우 재귀적으로 추가
                })) : [],
            };
        });

        return {
            props: { departments, additionalData: additionalDataMap },
        };
    } catch (error) {
        console.error('Error fetching data:', error);
        return {
            props: {
                departments: [],
                additionalData: {},
                error: 'Failed to load data. Please try again later.',
            },
        };
    }
};

const DepartmentPage = ({ departments, additionalData, error }: Props) => {
    if (error) {
        return <div>{error}</div>;
    }

    if (!departments || departments.length === 0) {
        return <div>Loading...</div>;  // 데이터 로딩 중 표시
    }

    return (
        <div>
            <h1>Department Structure</h1>
            {departments.map((dept) => renderDepartment(dept, additionalData))}
        </div>
    );
};

export default DepartmentPage;
