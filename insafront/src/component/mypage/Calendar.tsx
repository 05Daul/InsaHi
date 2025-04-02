// @/src/component/mypage/Calendar.tsx

import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import styles from "@/styles/mypage/Calendar.module.css";
import {CalendarDTO} from "@/api/mypage/calendaraction";


interface CalendarProps {
  leaveList: CalendarDTO[];
}

const Calendar = ({leaveList}: CalendarProps) => {
  const events = leaveList.map((leave) => ({
    title: "연차",
    start: leave.startDate,
    end: getNextDay(leave.stopDate), // FullCalendar는 end 날짜를 exclusive하게 처리함
    allDay: true,
  }));

  return (
      <div className={styles.calendarContainer}>
        <FullCalendar
            plugins={[dayGridPlugin]}
            initialView="dayGridMonth"
            headerToolbar={{
              left: "prev",
              center: "title",
              right: "next",
            }}
            events={events}
            aspectRatio={2}
            height="500px"
        />
      </div>
  );
};

// 마지막 날짜 포함을 위해 하루 더해주는 함수
function getNextDay(dateString: string) {
  const date = new Date(dateString);
  date.setDate(date.getDate() + 1); // 다음 날로 조정
  return date.toISOString().split("T")[0]; // YYYY-MM-DD 형식으로 반환
}

export default Calendar;
