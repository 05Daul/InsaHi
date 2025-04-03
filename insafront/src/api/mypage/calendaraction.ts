//캘린더 정보 가져오기
import {API_BASE_URL_Leave} from "@/api/api_base_url";

export default async function CalendarAction(employeeId: string, status: string): Promise<CalendarDTO[] | null> {
  const url = `${API_BASE_URL_Leave}/getemployeeleavel/${employeeId}/${status}`; //status 어케처리함?
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