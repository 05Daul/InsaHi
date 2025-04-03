//개인정보수정페이지
import {API_BASE_URL_Employee} from "@/api/api_base_url";

export default async function EmployeeInfoAction(employeeId: string): Promise<employeeInfoDTO | null> {
  const url = `${API_BASE_URL_Employee}/${employeeId}/employeeinfo`;
  try {

    let token = "";
    if (typeof window !== "undefined") {
      token = localStorage.getItem("accessToken") || "";
    }

    const response = await fetch(url,
        {
          method: "GET",
          headers: {
            // "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
          },
        });

    if (!response.ok) {
      console.log("if response" + response)
      throw new Error(`Failed to fetch data: ${response.status} ${response.statusText}`);
    }
    const data: employeeInfoDTO = await response.json();
    console.log(" profileinfo 성공함 ㅎ" + data);
    return data;
  } catch (err) {
    console.error("Error fetching EmployeeInfo FUCK:" + err);
    return null;
  }
}