'use client';

import {useRouter} from 'next/router';
import {useState, useEffect} from 'react';
import {useQuery, useMutation} from '@tanstack/react-query';
import {
  getLeaveDetailById,
  approveLeave,
  rejectLeave,
  approveAdditionalLeave, getRemainingLeave
} from '@/services/leaveService';
import {AnnualLeaveDTO, AnnualLeaveRequestDTO} from '@/type/leave';
import {toast} from 'react-toastify';
import styles from '@/styles/atdsal/leave.module.css';

const useLocalStorage = (key: string, defaultValue = '') => {
  const [value, setValue] = useState<string>(defaultValue);

  useEffect(() => {
    const storedValue = localStorage.getItem(key);
    setValue(storedValue ?? defaultValue);
  }, [key]);

  return value;
};

export default function LeaveDetailPage() {
  const router = useRouter();
  const {id} = router.query;
  const auth = useLocalStorage('auth');
  const leaveId = Array.isArray(id) ? parseInt(id[0]) : parseInt(id || '0');

  const {data: leave, isLoading, isError, refetch} = useQuery<AnnualLeaveRequestDTO>({
    queryKey: ['leaveDetail', leaveId],
    queryFn: () => getLeaveDetailById(Number(leaveId)),
    enabled: !!leaveId,
  });

  const {
    data: remainingLeave,
    isLoading: remainingLoading,
    isError: remainingError
  } = useQuery<AnnualLeaveDTO>({
    queryKey: ['remainingLeave', leave?.employeeId],
    queryFn: () => getRemainingLeave(leave!.employeeId),
    enabled: !!leave?.employeeId, // leave가 있고 employeeId도 있을 때만 실행
  });

  const approveMutation = useMutation({
    mutationFn: approveLeave,
    onSuccess: async () => {
      toast.success('휴가 승인 완료!');
      await refetch();
    },
  });

  const rejectMutation = useMutation({
    mutationFn: rejectLeave,
    onSuccess: async () => {
      toast.success('휴가 반려 완료!');
      await refetch();
    },
  });

  const additionalApproveMutation = useMutation({
    mutationFn: approveAdditionalLeave,
    onSuccess: async () => {
      toast.success('추가 연차 승인 완료!');
      await refetch();
    },
  });

  if (!id) return <p>파라미터가 없습니다</p>;
  if (auth === null) return <p className="text-center mt-10">권한 확인 중...</p>;
  if (!auth.includes('ROLE_ADMIN')) return <p className="text-red-500 text-center mt-10">접근 권한이
    없습니다.</p>;
  if (isLoading) return <p>로딩 중...</p>;
  if (isError || !leave) return <p className="text-red-500">상세 정보를 불러오지 못했습니다.</p>;

  const isFinalStatus = ['APPROVED', 'REJECTED', 'ADDITIONAL_APPROVED'].includes(leave.leaveApprovalStatus);

  return (
      <div className={`${styles.leaveDetailWrapper}`}>
        {/* 왼쪽: 정보 및 버튼 */}
        <div className={styles.leaveDetailLeft}>
          <h1 className={styles.leaveHeader}>휴가 신청 상세</h1>
          <div className={styles.leaveBox}>
            <p><strong>사번:</strong> {leave.employeeId}</p>
            <p><strong>회사 코드:</strong> {leave.companyCode}</p>
            <p>
              <strong>기간:</strong> {new Date(leave.startDate).toLocaleDateString()} ~ {new Date(leave.stopDate).toLocaleDateString()}
            </p>
            <p><strong>사유:</strong> {leave.reason}</p>
            <p><strong>현재 상태:</strong> {leave.leaveApprovalStatus}</p>
          </div>

          {!isFinalStatus && (
              <div className={styles.leaveButtonGroup}>
                <button onClick={() => approveMutation.mutate(leave)}
                        className={`${styles.leaveButton} `}>
                  승인
                </button>
                <button onClick={() => rejectMutation.mutate(leave)}
                        className={`${styles.leaveButton}`}>
                  거절
                </button>
                <button onClick={() => additionalApproveMutation.mutate(leave)}
                        className={`${styles.leaveButton} ${styles.leaveButton3}`}>
                  추가 승인
                </button>
              </div>
          )}

          <button onClick={() => router.back()} className={styles.leaveBackButton}>뒤로 가기</button>
        </div>

        {/* 오른쪽 해당 직원 연차 정보 */}
        <div className={styles.leaveDetailRight}>
          <h2 className={styles.leaveHeader}>직원 연차 내역</h2>
          {remainingLoading ? (
              <p>남은 연차 로딩 중...</p>
          ) : remainingError ? (
              <p className="text-red-500">남은 연차 조회 실패</p>
          ) : (
              <div className={styles.leaveBox}>
                <p>총 연차: <strong>{remainingLeave?.totalGrantedLeave ?? 0}</strong>일</p>
                <p>기본 연차: <strong>{remainingLeave?.baseLeave ?? 0}</strong>일</p>
                <p>추가 연차: <strong>{remainingLeave?.additionalLeave ?? 0}</strong>일</p>
                <p>사용한 연차: <strong>{remainingLeave?.usedLeave ?? 0}</strong>일</p>
                <p>남은 연차: <strong>{remainingLeave?.remainingLeave ?? 0}</strong>일</p>
              </div>
          )}

        </div>
      </div>
  );
}
