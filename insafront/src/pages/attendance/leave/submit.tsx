'use client';

import {useState, useEffect} from 'react';
import {useQuery, useMutation, useQueryClient} from '@tanstack/react-query';
import {
  submitLeave,
  getRemainingLeave,
  getLeaveUsageByEmployeeIdWithPagination
} from '@/services/leaveService';
import {toast} from 'react-toastify';
import {AnnualLeaveDTO, AnnualLeaveRequestDTO, PageResponseDTO} from '@/type/leave';
import styles from '@/styles/atdsal/leave.module.css';

const useLocalStorage = (key: string, defaultValue = '') => {
  const [value, setValue] = useState<string>(defaultValue);
  useEffect(() => {
    const storedValue = localStorage.getItem(key);
    setValue(storedValue ?? defaultValue);
  }, [key]);
  return value;
};

export default function LeaveSubmitPage() {
  const employeeId = useLocalStorage('employeeId', '');
  const companyCode = useLocalStorage('companyCode', '');
  const queryClient = useQueryClient();
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [form, setForm] = useState({startDate: '', stopDate: '', reason: ''});
  const [page, setPage] = useState(0);
  const size = 7;
  const today = new Date().toISOString().split('T')[0]; // 'YYYY-MM-DD' 형식

  const {
    data: remainingLeave,
    isLoading: remainingLoading,
    isError: remainingError
  } = useQuery<AnnualLeaveDTO>({
    queryKey: ['remainingLeave', employeeId],
    queryFn: () => getRemainingLeave(employeeId),
    enabled: Boolean(employeeId),
  });

  const {
    data,
    isLoading: usageLoading,
    isError: usageError
  } = useQuery<PageResponseDTO<AnnualLeaveRequestDTO>>({
    queryKey: ['usageList', employeeId, statusFilter, page],
    queryFn: () => getLeaveUsageByEmployeeIdWithPagination(employeeId, statusFilter, page, size),
    enabled: Boolean(employeeId),
  });

  const usageList = data?.content ?? [];
  const totalPages = data?.totalPages ?? 0;

  const leaveMutation = useMutation({
    mutationFn: (data: AnnualLeaveRequestDTO) => submitLeave(data),
    onSuccess: () => {
      toast.success('휴가 신청 완료!');
      queryClient.invalidateQueries({queryKey: ['usageList', employeeId]});
      queryClient.invalidateQueries({queryKey: ['remainingLeave', employeeId]});
      setForm({startDate: '', stopDate: '', reason: ''});
    },
    onError: () => toast.error('휴가 신청 실패!'),
  });

  const handleSubmit = () => {
    if (!employeeId || !companyCode) return toast.warning('로그인 정보가 없습니다.');
    if (!form.startDate || !form.stopDate || !form.reason) return toast.warning('모든 항목을 입력하세요.');

    const start = new Date(form.startDate);
    const stop = new Date(form.stopDate);
    const isOverlap = usageList.some((usage) => {
      const usageStart = new Date(usage.startDate);
      const usageStop = new Date(usage.stopDate);
      return (
          (start >= usageStart && start <= usageStop) ||
          (stop >= usageStart && stop <= usageStop) ||
          (start <= usageStart && stop >= usageStop)
      );
    });

    if (isOverlap) return toast.error('이미 신청된 기간과 겹칩니다.');

    leaveMutation.mutate({
      annualLeaveUsageId: 0,
      annualLeaveId: remainingLeave?.annualLeaveId || 0,
      employeeId,
      companyCode,
      startDate: start,
      stopDate: stop,
      reason: form.reason,
      leaveApprovalStatus: 'PENDING',
    });
  };

  const filteredUsageList = statusFilter === 'ALL'
      ? usageList
      : usageList.filter((usage) => usage.leaveApprovalStatus === statusFilter);

  return (
      <div className={styles.leaveSubmitWrapper}>
        {/* 왼쪽: 신청 폼 */}
        <div className={styles.leaveSubmitLeft}>
          <h1 className={styles.leaveHeader}>내 휴가 신청하기</h1>

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

          <div className={styles.leaveForm}>
            <input
                type="date"
                value={form.startDate}
                min={today} // 오늘 이전 선택 불가
                onChange={(e) => setForm({...form, startDate: e.target.value})}
                className={styles.leaveInput}/>
            <input
                type="date"
                value={form.stopDate}
                min={form.startDate || today} // 시작일 이후부터 선택 가능하게 처리
                onChange={(e) => setForm({...form, stopDate: e.target.value})}
                className={styles.leaveInput}
            /> <textarea placeholder="사유를 입력하세요" value={form.reason}
                         onChange={(e) => setForm({...form, reason: e.target.value})}
                         className={styles.leaveTextarea}/>
            <button onClick={handleSubmit} disabled={leaveMutation.isPending}
                    className={styles.leaveSubmitButton}>
              {leaveMutation.isPending ? '신청 중...' : '휴가 신청'}
            </button>
          </div>
        </div>

        {/* 오른쪽: 리스트 */}
        <div className={styles.leaveSubmitRight}>
          <div className={styles.leaveHistoryBox}>
            <div className={styles.leaveStatusFilter}>
              {['ALL', 'PENDING', 'APPROVED', 'REJECTED'].map((status) => (
                  <button
                      key={status}
                      onClick={() => {
                        setStatusFilter(status);
                        setPage(0);
                      }}
                      className={`${styles.leaveStatusFilterButton} ${statusFilter === status ? styles.active : ''}`}
                  >
                    {status}
                  </button>
              ))}
            </div>

            {usageLoading ? (
                <p>신청 내역 로딩 중...</p>
            ) : usageError ? (
                <p className="text-red-500">신청 내역 조회 실패</p>
            ) : (
                <ul className="space-y-2">
                  {filteredUsageList.length === 0 ? (
                      <li className={styles.leaveSubmitCard}>휴가 신청 내역이 없습니다.</li>
                  ) : (
                      filteredUsageList.map((usage) => (
                          <li key={usage.annualLeaveUsageId} className={styles.leaveSubmitCard}>
                            <div className={styles.leaveSubmitCardDate}>
                              날짜: {new Date(usage.startDate).toLocaleDateString()} ~ {new Date(usage.stopDate).toLocaleDateString()}
                            </div>

                            <div
                                className={`${styles.leaveSubmitCardStatus} ${
                                    usage.leaveApprovalStatus === 'APPROVED'
                                        ? 'text-green-500'
                                        : usage.leaveApprovalStatus === 'REJECTED'
                                            ? 'text-red-500'
                                            : 'text-yellow-500'
                                }`}
                            >
                              상태: {usage.leaveApprovalStatus}
                            </div>

                            <div className={styles.leaveSubmitCardReason}>사유: {usage.reason}</div>
                            <div className={styles.leaveSubmitCardEmpty}></div>
                          </li>
                      ))
                  )}
                </ul>

            )}

            <div className={styles.leavePagination}>
              <button onClick={() => setPage((prev) => prev - 1)} disabled={page <= 0}
                      className="p-2 bg-gray-300 rounded">이전
              </button>
              <span>{page + 1} / {totalPages}</span>
              <button onClick={() => setPage((prev) => prev + 1)} disabled={page + 1 >= totalPages}
                      className="p-2 bg-gray-300 rounded">다음
              </button>
            </div>
          </div>
        </div>
      </div>
  );
}
