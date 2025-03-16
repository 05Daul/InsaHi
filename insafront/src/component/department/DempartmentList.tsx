// pages/[companyCode]/department/list.tsx
import { GetServerSideProps } from 'next';
import { useState } from 'react';
import { Department } from '@/type/Employee';
import DepartmentView from '@/component/department/DepartmentView'; // 부서 렌더링 함수
import { getDepartmentList } from '@/lib/getDepartmentList';

interface Props {
    initialDepartments: Department[];
    error?: string;
}

const DepartmentListPage = ({ initialDepartments, error }: Props) => {
    const [departments, setDepartments] = useState<Department[]>(initialDepartments);


    if (error) {
        return <div>{error}</div>;
    }

    return (
        <div>
            <h1>조직도</h1>
            {departments && departments.length > 0 ? (
                departments.map((dept) => <DepartmentView key={dept.departmentId} department={dept} />)
            ) : (
                <div>부서가 없습니다.</div>
            )}
        </div>
    );
};

export const getServerSideProps: GetServerSideProps = async (context) => {
    const { companyCode } = context.query;

    try {

        const departments = await getDepartmentList(companyCode as string);
        console.log('getServerSideProps ', departments);

        return {
            props: { initialDepartments: departments },
        };
    } catch (error) {
        console.error('데이터 오류', error);
        return {
            props: {
                initialDepartments: [],
                error: "데이터 로드에 실패했습니다. 나중에 다시 시도해 주세요.",
            },
        };
    }
};

export default DepartmentListPage;
