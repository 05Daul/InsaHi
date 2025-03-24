
import {profileCardDTO} from "@/type/profilecard";

//
export default async function ProfileCard(employeeId:string):Promise<profileCardDTO | null>{
    const url = `http://127.0.0.1:1006/employee/${employeeId}/profilecard`;
    console.log(employeeId);
    try {

        let token = "";
        if (typeof window !== "undefined") {
            token = localStorage.getItem("accessToken") || "";
        } // 🔥 JWT 토큰 가져오기
        console.log("what the fuck ~~ token ++++"+token);
        const response =  await fetch(url,
            {
                method:"GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
            });

        if(!response.ok){
            console.log("if response"+response)
            throw new Error(`Failed to fetch data: ${response.status} ${response.statusText}`);
        }
        const data: profileCardDTO = await response.json();
        return data;
    }
    catch (err){
        console.error("Error fetching profileCard ERRRRRR FFFFUUUCCKKK:"+err);
        return null;
    }
}
