
// pages/api/[companyCode]/department/departmentList.ts

import type { NextApiRequest, NextApiResponse } from 'next';
import { getDepartmentList } from '@/lib/getDepartmentList'; // 외부 API 호출 함수

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    const { companyCode } = req.query;

    try {
        const departments = await getDepartmentList(companyCode as string);
        res.status(200).json(departments);
    } catch (error: any) {
        console.error('API 에러:', error.message);
        res.status(500).json({ message: '서버에서 부서 데이터를 가져오는 데 실패했습니다.' });
    }
}
