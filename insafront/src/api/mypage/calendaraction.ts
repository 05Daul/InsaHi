//캘린더 정보 가져오기
export default async function CalendarAction(employeeId: string, status: string): Promise<CalendarDTO[] | null> {
  const url = `http://127.0.0.1:1006/leave/getemployeeleavel/${employeeId}/${status}`; //status 어케처리함?
  try {

    let token = "";
    if (typeof window !== "undefined") {
      token = localStorage.getItem("accessToken") || "";
    }

    const response = await fetch(url,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
          },
        });

    if (!response.ok) {
      console.log("if response" + response)
      throw new Error(`Failed to fetch data: ${response.status} ${response.statusText}`);
    }
    const data: CalendarDTO[] = await response.json();
    console.log(" profileCalendar 성공함 ㅎ" + data);
    return data;
  } catch (err) {
    console.error("CalendarAction error::" + err);
    return null;
  }
}

export interface CalendarDTO {
  employeeId: string;
  startDate: string;
  stopDate: string;
  status: string;
}