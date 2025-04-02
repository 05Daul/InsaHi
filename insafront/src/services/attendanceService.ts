import {attendanceFetcher} from '@/api/serviceFetcher/attendanceFetcher';
import {AttendanceEntity} from '@/type/Attendance';

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
    console.error('퇴근 처리가 되지 않았습니다. 퇴근 처리를 먼저 해주세요');
    // 여기에 추가적인 오류 정보를 사용자에게 제공하도록 수정할 수 있음
    throw new Error('퇴근 처리가 되지 않았습니다. 퇴근 처리를 먼저 해주세요');
  }
};

export const checkOut = async (employeeId: string): Promise<void> => {
  try {
    const response = await attendanceFetcher(`/check-out?employeeId=${employeeId}`, {
      method: 'PUT',
    });

    // 빈 응답 처리
    if (response === undefined || response === null) {
      console.log('서버에서 응답을 받지 못했습니다.');
      return;
    }
    console.log('퇴근 처리 완료');
  } catch (error) {
    console.error('퇴근 처리 실패:', error);
    throw new Error('응답이 없습니다.');
  }
};
