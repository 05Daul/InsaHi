'use client';

import {useEffect, useState} from 'react';
import {useQuery} from '@tanstack/react-query';
import {getLeaveUsageByCompanyWithPagination} from '@/services/leaveService';
import {AnnualLeaveRequestDTO} from '@/type/leave';
import Link from 'next/link';
import styles from '@/styles/atdsal/leave.module.css';

const useLocalStorage = (key: string, defaultValue = '') => {
  const [value, setValue] = useState<string>(defaultValue);
  useEffect(() => {
    const storedValue = localStorage.getItem(key);
    setValue(storedValue ?? defaultValue);
  }, [key]);
  return value;
};

export default function LeaveRequestPage() {
  const companyCode = useLocalStorage('companyCode');
  const auth = useLocalStorage('auth');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [page, setPage] = useState(0);
  const size = 10;

  const {data, isLoading, isError} = useQuery({
    queryKey: ['usageList', companyCode, statusFilter, page],
    queryFn: () => getLeaveUsageByCompanyWithPagination(companyCode || '', statusFilter, page, size),
    enabled: companyCode !== null,
  });

  const usageList = data?.content ?? [];
  const totalPages = data?.totalPages ?? 0;

  if (auth === null || companyCode === null) return <p className="text-center mt-10">권한 확인 중...</p>;
  if (!auth.includes('ROLE_ADMIN')) return <p className="text-center text-red-500 mt-10">접근 권한이
    없습니다.</p>;

  return (
      <div className={styles.leaveContainer}>
        <h1 className={styles.leaveHeader}>휴가 신청 내역 관리</h1>

        <div className={styles.leaveStatusFilter}>
          {['ALL', 'PENDING', 'APPROVED', 'REJECTED'].map((status) => (
              <button
                  key={status}
                  onClick={() => {
                    setStatusFilter(status);
                    setPage(0);
                  }}
                  className={`${styles.leaveStatusFilterButton} ${
                      statusFilter === status ? styles.active : ''
                  }`}
              >
                {status}
              </button>
          ))}
        </div>


        {isLoading && <p>데이터 로딩 중...</p>}
        {isError && <p className="text-red-500">데이터 조회 실패</p>}

        <ul className="space-y-2">
          {usageList.length === 0 ? (
              <li className="text-gray-500">휴가 신청 내역이 없습니다.</li>
          ) : (
              usageList.map((usage: AnnualLeaveRequestDTO) => (
                  <li key={usage.annualLeaveUsageId} className={styles.leaveCard}>
                    <div className={styles.leaveCardInfo}>
                      <span><strong>사번:</strong> {usage.employeeId}</span>
                      <span><strong>기간:</strong> {new Date(usage.startDate).toLocaleDateString()} ~ {new Date(usage.stopDate).toLocaleDateString()}</span>
                      <span className={`${styles.leaveCardStatus} ${
                          usage.leaveApprovalStatus === 'APPROVED'
                              ? 'text-green-500'
                              : usage.leaveApprovalStatus === 'REJECTED'
                                  ? 'text-red-500'
                                  : 'text-yellow-500'
                      }`}>
                        <strong>상태:</strong>
                        {usage.leaveApprovalStatus} </span>
                    </div>
                    <Link href={`/attendance/leave/detail/${usage.annualLeaveUsageId}`}
                          className={styles.leaveCardButton}> 상세 보기
                    </Link>
                  </li>
              ))
          )}
        </ul>

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
  );
}
