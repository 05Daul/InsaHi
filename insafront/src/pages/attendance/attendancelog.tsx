import React, {useEffect, useState} from 'react';
import {useQuery, useQueryClient} from '@tanstack/react-query';
import styles from '../../styles/atdsal/attendancelog.module.css';
import {fetchAttendanceRecords} from '@/services/attendanceService';
import {AttendanceEntity} from '@/type/Attendance';
import {profileCardDTO} from "@/type/profilecard";
import AttendanceActions from '../../services/attendanceAction';

// 시간 포맷 함수
const formatTime = (time: string | null): string => {
  if (!time) return '-';
  const date = new Date(time);
  const hours = date.getHours().toString().padStart(2, '0');
  const minutes = date.getMinutes().toString().padStart(2, '0');
  return `${hours}:${minutes}`;
};

// 년, 월별로 근무 기록을 그룹화하는 함수
const groupByYearAndMonth = (records: AttendanceEntity[]) => {
  const grouped: Record<string, Record<string, AttendanceEntity[]>> = {};

  records.forEach(record => {
    const yearMonth = new Date(record.workDate);
    const year = yearMonth.getFullYear();
    const month = yearMonth.getMonth() + 1; // 월은 0부터 시작하므로 +1

    const yearMonthKey = `${year}-${month.toString().padStart(2, '0')}`;

    if (!grouped[year]) {
      grouped[year] = {};
    }
    if (!grouped[year][month]) {
      grouped[year][month] = [];
    }

    grouped[year][month].push(record);
  });

  return grouped;
};

const AttendanceLog: React.FC = () => {
  const [userData, setUserData] = useState<profileCardDTO | null>(null);
  const [employeeId, setEmployeeId] = useState<string>('defaultID');
  const [selectedYear, setSelectedYear] = useState<number | null>(null); // 선택된 연도
  const [selectedMonth, setSelectedMonth] = useState<number | null>(null); // 선택된 월
  const [currentPage, setCurrentPage] = useState(1); // 현재 페이지
  const itemsPerPage = 10; // 페이지당 항목 수

  useEffect(() => {
    const storedEmployeeId = localStorage.getItem('employeeId');
    if (storedEmployeeId) {
      setEmployeeId(storedEmployeeId);
    }

    const today = new Date();
    setSelectedYear(today.getFullYear());
    setSelectedMonth(today.getMonth() + 1); // 월은 0부터 시작하므로 +1
  }, []);

  // QueryClient 가져오기
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

  const handleYearChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedYear(Number(event.target.value));
    setSelectedMonth(null); // 년도를 변경하면 월을 초기화
  };

  const handleMonthChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedMonth(Number(event.target.value));
  };

  if (isLoading) return <div>근무 기록을 불러오는 중입니다...</div>;
  if (isError) return <div>에러 발생: {error?.message}</div>;

  // 년도와 월로 근무 기록을 그룹화
  const groupedAttendanceRecords = groupByYearAndMonth(attendanceRecords);

  // 가능한 연도 리스트 (기본적으로 2020부터 현재 연도까지)
  const years = Array.from({length: new Date().getFullYear() - 2019}, (_, i) => 2020 + i);

  // 선택된 연도와 월에 맞는 근무 기록 필터링
  const filteredRecords =
      selectedYear && selectedMonth
          ? groupedAttendanceRecords[selectedYear]?.[selectedMonth] || []
          : [];

  // 현재 페이지에 맞는 데이터만 가져오기
  const indexOfLastRecord = currentPage * itemsPerPage;
  const indexOfFirstRecord = indexOfLastRecord - itemsPerPage;
  const currentRecords = filteredRecords.slice(indexOfFirstRecord, indexOfLastRecord);

  // 페이지 버튼 생성
  const totalPages = Math.ceil(filteredRecords.length / itemsPerPage);
  const pageNumbers = [];
  for (let i = 1; i <= totalPages; i++) {
    pageNumbers.push(i);
  }

  return (
      <div className={styles.pageWrapper}>
        <h2 className={styles.title}>월별 근무시간 현황</h2>
        <AttendanceActions
            employeeId={employeeId}
            attendanceRecords={attendanceRecords}
        />

        {/* 년도와 월을 한 줄에 나란히 표시 */}
        <div className={styles.selectWrapper}>
          <label>연도:</label>
          <select onChange={handleYearChange} value={selectedYear || ''}>
            <option value="">연도 선택</option>
            {years.map((year) => (
                <option key={year} value={year}>
                  {year}
                </option>
            ))}
          </select>

          {selectedYear && (
              <div className={styles.selectWrapper}>
                <label>월:</label>
                <select onChange={handleMonthChange} value={selectedMonth || ''}>
                  <option value="">월 선택</option>
                  {Array.from({length: 12}, (_, i) => i + 1).map((month) => (
                      <option key={month} value={month}>
                        {month}월
                      </option>
                  ))}
                </select>
              </div>
          )}
        </div>

        {/* 선택된 연도와 월에 맞는 근무 기록 출력 */}
        {currentRecords.length > 0 ? (
            <div>
              <h3> 근무 기록</h3>
              <table className={styles.workLogTable}>
                <thead>
                <tr>
                  <th>일자</th>
                  <th>사원코드</th>
                  <th>출근시간</th>
                  <th>퇴근시간</th>
                  <th>근무시간(분 단위)</th>
                  <th>연장근무시간(시간 단위)</th>
                </tr>
                </thead>
                <tbody>
                {currentRecords.map((record: AttendanceEntity) => (
                    <tr key={record.employeeId}>
                      <td>{record.workDate ?? '-'}</td>
                      <td>{record.employeeId ?? '-'}</td>
                      <td>{formatTime(record.checkInTime)}</td>
                      <td>{formatTime(record.checkOutTime)}</td>
                      <td>{record.workHours ?? '-'}</td>
                      <td>{record.overtimeHours ?? '-'}</td>
                    </tr>
                ))}
                </tbody>
              </table>

              {/* 페이지네이션 */}
              <div className={styles.pagination}>
                {pageNumbers.map((number) => (
                    <button
                        key={number}
                        onClick={() => setCurrentPage(number)}
                        className={currentPage === number ? styles.active : ''}
                    >
                      {number}
                    </button>
                ))}
              </div>
            </div>
        ) : (
            <div>선택된 년도와 월에 대한 근무 기록이 없습니다.</div>
        )}
      </div>
  );
};

export default AttendanceLog;