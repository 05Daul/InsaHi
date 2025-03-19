import {profileCardDTO} from "@/type/profilecard";


export default async function smallProfile(employeeId:string):Promise<profileCardDTO | null>{
    const url = `http://127.0.0.1:1006/employee/${employeeId}/profilecard`;

    console.log(employeeId);

    try {

        const token = localStorage.getItem("accessToken"); // 🔥 JWT 토큰 가져오기
        console.log("what the fuck ~~ token ++++=="+token);
        const response =  await fetch(url,
            {
                method:"GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
            });
        console.log("response rere"+response)
        console.log("response 이후 =============="+token)
        if(!response.ok){
            console.log("if response"+response)
            throw new Error(`Failed to fetch data: ${response.status} ${response.statusText}`);
        }
        const data: profileCardDTO = await response.json();
        console.log("hihihihihihihi"+data);
        return data;
    }
    catch (err){
        console.error("Error fetching small profile:"+err);
        return null;
    }
}