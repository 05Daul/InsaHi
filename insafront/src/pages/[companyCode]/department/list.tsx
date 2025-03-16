// pages/[companyCode]/department/list.tsx
import { GetServerSideProps } from 'next';
import { useState } from 'react';
import { Department } from '@/type/Employee';
import DepartmentView from '@/component/department/DepartmentView'; // 부서 렌더링 함수
import DepartmentSide from '@/component/sidebar/DepartmentSide'; // 사이드바 컴포넌트 임포트
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
        <div className="flex min-h-screen">
            {/* 사이드바 */}
            <aside className="w-1/4 p-4">
                <DepartmentSide />
            </aside>

            {/* 메인 콘텐츠 */}
            <main className="w-3/4 p-4">
                <h1 className="text-2xl font-bold mb-6">조직도</h1>
                {departments && departments.length > 0 ? (
                    departments.map((dept) => <DepartmentView key={dept.departmentId} department={dept} />)
                ) : (
                    <div>부서가 없습니다.</div>
                )}
            </main>
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
