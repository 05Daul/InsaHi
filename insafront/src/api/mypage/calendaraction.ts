// //개인정보수정페이지
// export default async function calendarAction(employeeId: string): Promise<string | null> {
//   const url = `http://127.0.0.1:1006/`;
//   try {
//
//     let token = "";
//     if (typeof window !== "undefined") {
//       token = localStorage.getItem("accessToken") || "";
//     }
//
//     const response = await fetch(url,
//         {
//           method: "GET",
//           headers: {
//             "Content-Type": "application/json",
//             "Authorization": `Bearer ${token}`
//           },
//         });
//
//     if (!response.ok) {
//       console.log("if response" + response)
//       throw new Error(`Failed to fetch data: ${response.status} ${response.statusText}`);
//     }
//     const data: employeeInfoDTO = await response.json();
//     console.log(" profileinfo 성공함 ㅎ" + data);
//     return data;
//   } catch (err) {
//     console.error("Error fetching EmployeeInfo FUCK:" + err);
//     return null;
//   }
// }