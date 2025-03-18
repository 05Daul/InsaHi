import {SmallprofileDTO} from "@/type/smallprofile";


export default async function smallProfile(employeeId:string):Promise<SmallprofileDTO | null>{
    const url = `http://127.0.0.1:1006/employee/${employeeId}/smallprofile`;

    console.log(employeeId);

    try {

        const token = localStorage.getItem("accessToken"); // 🔥 JWT 토큰 가져오기
        const response =  await fetch(url,
            {
                method:"GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
            });
        console.log("response rere"+response)
        if(!response.ok){
            console.log("if response"+response)
            throw new Error(`Failed to fetch data: ${response.status} ${response.statusText}`);
        }
        const data: SmallprofileDTO = await response.json();
        console.log("hihihihihihihi"+data);
        return data;
    }
    catch (err){
        console.error("Error fetching small profile:"+err);
        return null;
    }
}