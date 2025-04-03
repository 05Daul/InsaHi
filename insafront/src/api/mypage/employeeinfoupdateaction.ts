//개인정보수정페이지
import {API_BASE_URL_Employee} from "@/api/api_base_url";

export default async function EmployeeInfoUpdateAction(employeeId: string, formData: FormData): Promise<employeeInfoDTO | null> {
  const url = `${API_BASE_URL_Employee}/${employeeId}/update`;
  try {

    let token = "";
    if (typeof window !== "undefined") {
      token = localStorage.getItem("accessToken") || "";
    }

    const response = await fetch(url,
        {
          method: "PUT",
          headers: {
            // "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
          },
          body: formData,
          // body: JSON.stringify({
          //   email,
          //   phoneNumber,
          // }),
        });

    if (!response.ok) {
      console.log("if response" + response)
      throw new Error(`Failed to fetch data: ${response.status} ${response.statusText}`);
    }
    const data: employeeInfoDTO = await response.json();
    console.log("profileinfo update 성공함 ㅎ " + data);
    return data;
  } catch (err) {
    console.error("Error fetching EmployeeInfo FUCK:" + err);
    return null;
  }
}