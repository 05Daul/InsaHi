//개인정보수정페이지
export default async function EmployeeInfoAction(employeeId:string):Promise<employeeInfoDTO | null>{
    const url = `http://127.0.0.1:1006/employee/${employeeId}/update`;
    try {

        let token = "";
        if (typeof window !== "undefined") {
            token = localStorage.getItem("accessToken") || "";
        }

        const response = await fetch(url,
            {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
            });

        if (!response.ok) {
            console.log("if response" + response)
            throw new Error(`Failed to fetch data: ${response.status} ${response.statusText}`);
        }
        const data: employeeInfoDTO = await response.json();
        console.log("fucking data"+data);
        return data;
    }
    catch (err){
        console.error("Error fetching EmployeeInfo FUCK:"+err);
        return null;
    }
}