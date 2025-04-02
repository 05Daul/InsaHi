// pages/approval/DocumentsPage.tsx
import React, {useState, useEffect} from 'react';
import Link from 'next/link';
import {useInfiniteQuery} from '@tanstack/react-query';
import {useInView} from 'react-intersection-observer';
import StatusBadge from '@/component/approval/StatusBadge';
import useSSE from '@/component/approval/useSSE';
import styles from '@/styles/approval/Documents.module.css';
import Toast from "@/component/approval/Toast";
import {ApprovalStatus} from "@/type/ApprovalStatus";

// Document 타입에 ApprovalStatus 적용
interface Document {
  id: string;
  name: string;
  text: string;
  employeeId: string;
  status: ApprovalStatus;
  createdAt: string;
}

// 로컬스토리지 유틸 그대로 사용
const useLocalStorage = (key: string, defaultValue: string | null = null) => {
  const [storedValue, setStoredValue] = useState<string | null>(defaultValue);

  useEffect(() => {
    if (typeof window !== 'undefined') {
      const value = localStorage.getItem(key);
      setStoredValue(value);
    }
  }, [key]);

  return storedValue;
};

const DocumentsPage = () => {
  const [menu, setMenu] = useState<number>(1);
  const [filterStatus, setFilterStatus] = useState<ApprovalStatus | 'ALL'>('ALL');
  const [sortOrder, setSortOrder] = useState<'ASC' | 'DESC'>('DESC');
  const [searchQuery, setSearchQuery] = useState('');
  const [toastMessage, setToastMessage] = useState<string | null>(null);
  const employeeId = useLocalStorage('employeeId', 'defaultId');

  // SSE 알림 수신 - useSSE() 제거
  useEffect(() => {
    const handleSseNotify = () => {
      refetch(); // 문서 리스트 다시 가져오기
    };

    window.addEventListener('sse-notify', handleSseNotify);

    return () => {
      window.removeEventListener('sse-notify', handleSseNotify);
    };
  }, []);

  const {
    data,
    fetchNextPage,
    hasNextPage,
    isFetchingNextPage,
    refetch,
    isLoading,
    error
  } = useInfiniteQuery({
    queryKey: ['documents', employeeId, menu, filterStatus, sortOrder],
    initialPageParam: 0,
    queryFn: async ({pageParam = 0}) => {
      if (!employeeId) return {content: [], last: true};

      const statusParam = filterStatus === 'ALL' ? '' : `&status=${filterStatus}`;
      const sortParam = `&sort=createdAt,${sortOrder}`;

      const response = await fetch(
          `http://127.0.0.1:1006/approval/list/${employeeId}/${menu}?page=${pageParam}&size=10${statusParam}${sortParam}`
      );

      if (!response.ok) throw new Error('문서 목록 불러오기 실패');
      return response.json();
    },
    getNextPageParam: (lastPage, pages) => {
      if (lastPage.last) return undefined;
      return pages.length;
    },
    enabled: !!employeeId
  });

  const {ref, inView} = useInView();

  useEffect(() => {
    if (inView && hasNextPage && !isFetchingNextPage) {
      fetchNextPage();
    }
  }, [inView, hasNextPage, isFetchingNextPage]);

  return (
      <div className={styles.documentsPageContainer}>
        <div className={styles.documentsMainContent}>
          <h1 className={styles.documentsPageTitle}>결재 문서 목록</h1>

          {/* 메뉴 필터 */}
          <div className={styles.documentsFilterContainer}>
            <button
                className={menu === 1 ? styles.documentsCreateButton : styles.documentsPaginationButton}
                onClick={() => setMenu(1)}
            >
              본인이 작성한 문서
            </button>
            <button
                className={menu === 2 ? styles.documentsCreateButton : styles.documentsPaginationButton}
                onClick={() => setMenu(2)}
            >
              결재자로 지정된 문서
            </button>
            <button
                className={menu === 3 ? styles.documentsCreateButton : styles.documentsPaginationButton}
                onClick={() => setMenu(3)}
            >
              결재해야 할 문서
            </button>
          </div>

          {/* 검색 기능 */}
          <input
              type="text"
              placeholder="문서 이름 검색"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className={styles.documentsSearchInput}
          />

          {/* 상태 필터 및 정렬 */}
          <div className={styles.documentsFilterContainer}>
            <label>
              상태 필터:
              <select
                  value={filterStatus}
                  onChange={(e) => setFilterStatus(e.target.value as ApprovalStatus | 'ALL')}
              >
                <option value="ALL">전체</option>
                <option value={ApprovalStatus.APPROVED}>승인됨</option>
                <option value={ApprovalStatus.REJECTED}>반려됨</option>
                <option value={ApprovalStatus.PENDING}>진행 중</option>
              </select>
            </label>

            <label>
              정렬:
              <select
                  value={sortOrder}
                  onChange={(e) => setSortOrder(e.target.value as 'ASC' | 'DESC')}
              >
                <option value="DESC">최신순</option>
                <option value="ASC">오래된순</option>
              </select>
            </label>
          </div>

          {/* 로딩 & 에러 */}
          {isLoading && <p className={styles.documentsLoading}>문서를 불러오는 중입니다...</p>}
          {error && <p className={styles.documentsError}>오류 발생: {error.message}</p>}

          {/* 문서 목록 */}
          <div>
            <h2>문서 목록</h2>
            <ul>
              {!isLoading && !error && data?.pages.length === 0 && (
                  <p className={styles.documentsEmptyState}>조회된 문서가 없습니다.</p>
              )}

              {data?.pages.map((page, pageIndex) => (
                  <React.Fragment key={pageIndex}>
                    {page.content
                    .filter((doc: Document) =>
                        doc.name.toLowerCase().includes(searchQuery.toLowerCase())
                    )
                    .map((doc: Document) => (
                        <li key={doc.id} className={styles.documentsTable}>
                          <Link
                              href={{
                                pathname: `/approval/file/${doc.id}`,
                                query: {menu}
                              }}
                              className={styles.documentsActionLink}
                          >
                            <span>{doc.name}</span>
                            <StatusBadge status={doc.status}/>
                          </Link>
                        </li>
                    ))}
                  </React.Fragment>
              ))}

              {/* 무한 스크롤 트리거 */}
              <li ref={ref} className={styles.documentsLoadingTrigger}>
                {isFetchingNextPage
                    ? '불러오는 중...'
                    : hasNextPage
                        ? '스크롤하여 더 보기'
                        : '더 이상 데이터 없음'}
              </li>
            </ul>
          </div>
        </div>

        {/* 토스트 메시지 */}
        {toastMessage && (
            <Toast message={toastMessage} onClose={() => setToastMessage(null)}/>
        )}
      </div>
  );
};

export default DocumentsPage;


