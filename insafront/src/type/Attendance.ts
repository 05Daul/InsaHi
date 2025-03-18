export interface AttendanceEntity {
    id: number;
    employeeId: string;
    companyCode: string;
    attendanceDate: string; // 또는 string (예: 'YYYY-MM-DD')
    checkInTime: string; // 시간은 보통 string (예: '09:00')
    checkOutTime: string; // 시간은 보통 string (예: '18:00')
    workHours: number;
    overtimeHours: number;
    status: string; // 출근 상태 (예: '출근', '지각', '결근')
}