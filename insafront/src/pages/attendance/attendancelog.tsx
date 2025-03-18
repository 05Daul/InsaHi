import React from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import styles from '../../styles/attendancelog.module.css';
import { fetchAttendanceRecords, checkIn, checkOut } from '@/services/attendanceService'
import { AttendanceEntity } from '@/type/Attendance';

const AttendanceLog: React.FC = () => {
    const employeeId: string = 'E001';
    const queryClient = useQueryClient();

    const {
        data: attendanceRecords = [],
        isLoading,
        isError,
        error,
    } = useQuery<AttendanceEntity[], Error>({
        queryKey: ['attendanceRecords', employeeId],
        queryFn: () => fetchAttendanceRecords(employeeId),
    });

    //  출근
    const checkInMutation = useMutation<void, Error>({
        mutationFn: () => checkIn(employeeId),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['attendanceRecords', employeeId] });
        },
        onError: (error) => {
            alert(`출근 실패: ${error.message}`);
        },
    });

    // ✅ 퇴근 mutation
    const checkOutMutation = useMutation<void, Error, string>({
        mutationFn: (employeeId: string) => checkOut(employeeId),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['attendanceRecords', employeeId] });
        },
        onError: (error) => {
            alert(`퇴근 실패: ${error.message}`);
        },
    });

    // ✅ 버튼 이벤트 핸들러
    const handleCheckIn = () => {
        checkInMutation.mutate();
    };

    const handleCheckOut = () => {
        const todayRecord = attendanceRecords.find((record) => !record.checkOutTime);
        if (todayRecord) {
            checkOutMutation.mutate(todayRecord.employeeId);
        } else {
            alert('오늘 출근 기록이 없습니다!');
        }
    };

    // ✅ 로딩 상태 처리
    if (isLoading) return <div>근무 기록을 불러오는 중입니다...</div>;
    if (isError) return <div>에러 발생: {error?.message}</div>;

    return (
        <div className={styles.pageWrapper}>
            <h2 className={styles.title}>일별 근무시간 현황</h2>

            <div className={styles.buttonWrapper}>
                <button
                    onClick={handleCheckIn}
                    className={styles.button}
                    disabled={checkInMutation.isPending}
                >
                    {checkInMutation.isPending ? '출근 중...' : '출근'}
                </button>

                <button
                    onClick={handleCheckOut}
                    className={styles.button}
                    disabled={checkOutMutation.isPending}
                >
                    {checkOutMutation.isPending ? '퇴근 중...' : '퇴근'}
                </button>
            </div>

            <table className={styles.workLogTable}>
                <thead>
                <tr>
                    <th>일자</th>
                    <th>사원코드</th>
                    <th>출근시간</th>
                    <th>퇴근시간</th>
                    <th>근무시간</th>
                </tr>
                </thead>
                <tbody>
                {attendanceRecords.map((record: AttendanceEntity) => (
                    <tr key={record.employeeId}>
                        <td>{record.attendanceDate ?? '-'}</td>
                        <td>{record.employeeId ?? '-'}</td>
                        <td>{record.checkInTime ?? '-'}</td>
                        <td>{record.checkOutTime ?? '-'}</td>
                        <td>{record.workHours ?? '-'}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default AttendanceLog;