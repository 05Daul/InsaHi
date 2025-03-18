import { attendanceFetcher } from '@/api/serviceFetcher/attendanceFetcher';
import { AttendanceEntity } from '@/type/Attendance';

// 근무 기록 조회 (GET)
export const fetchAttendanceRecords = async (employeeId: string): Promise<AttendanceEntity[]> => {
    try {
        return await attendanceFetcher<AttendanceEntity[]>(`/checkin/${employeeId}`);
    } catch (error) {
        console.error('Failed to fetch attendance records:', error);
        throw error;
    }
};

// 출근 처리 (POST)
export const checkIn = async (employeeId: string): Promise<void> => {
    try {
        return await attendanceFetcher<void>(`/checkin?employeeId=${employeeId}`, {
            method: 'POST',
        });
    } catch (error) {
        console.error('Check-in failed:', error);
        throw error;
    }
};

// 퇴근 처리 (PUT)
export const checkOut = async (employeeId: string): Promise<void> => {
    try {
        return await attendanceFetcher<void>(`/check-out`, {
            method: 'PUT',
        });
    } catch (error) {
        console.error('Check-out failed:', error);
        throw error;
    }
};