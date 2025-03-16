import { NextApiRequest, NextApiResponse } from 'next';

// 가상의 데이터베이스 예시 (실제로는 데이터베이스를 사용해야 함)
const employees = [
    { employeeId: 'E01', employeeName: '앨리스', status: 'ACTIVE' },
    { employeeId: 'E02', employeeName: '밥', status: 'INACTIVE' },
];

// 직원 상태 업데이트
export default function handler(req: NextApiRequest, res: NextApiResponse) {
    const { employeeId } = req.query;
    const { status } = req.body;

    const employee = employees.find((emp) => emp.employeeId === employeeId);

    if (!employee) {
        return res.status(404).json({ error: 'Employee not found' });
    }

    // 상태 변경
    employee.status = status;

    return res.status(200).json(employee);
}
