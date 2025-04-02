import React from 'react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { checkIn, checkOut } from '@/services/attendanceService'; // 서비스 임포트
import styles from '../styles/atdsal/attendancelog.module.css';

interface AttendanceActionsProps {
    employeeId: string;
    attendanceRecords: any[];
}

const AttendanceActions: React.FC<AttendanceActionsProps> = ({
                                                                 employeeId,
                                                                 attendanceRecords,
                                                             }) => {
    // queryClient는 이곳에서 직접 가져옵니다.
    const queryClient = useQueryClient();

    // 출근 상태 확인
    const checkInMutation = useMutation<void, Error>({
        mutationFn: () => checkIn(employeeId),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['attendanceRecords', employeeId] });
            alert('출근 완료');
        },
        onError: (error) => {
            alert(`출근 실패: ${error.message}`);
        },
    });

    // 퇴근 상태 확인
    const checkOutMutation = useMutation<void, Error, string>({
        mutationFn: (employeeId: string) => checkOut(employeeId),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['attendanceRecords', employeeId] });
            alert('퇴근 완료');
        },
        onError: (error) => {
            if (error.message === '응답이 없습니다.') {
                alert('서버에서 응답을 받지 못했습니다.');
            } else {
                alert(`퇴근 실패: ${error.message}`);
            }
        },
    });

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

    return (
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
    );
};

export default AttendanceActions;