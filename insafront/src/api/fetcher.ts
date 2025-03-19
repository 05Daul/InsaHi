// import {redirect} from "next/navigation";
//
// export async function fetcher(url: string, options: RequestInit = {}, context?: any) {
//     let token = "";
//
//     if (typeof window !== "undefined") {
//         // 클라이언트(CSR)에서 실행될 때만 localStorage 사용
//         token = localStorage.getItem("accessToken") || "";
//     } else if (context?.req?.cookies?.accessToken) {
//         // 서버(SSR)에서 실행될 때는 쿠키에서 토큰 가져오기
//         token = context.req.cookies.accessToken;
//     }
//
//     const headers = {
//         "Content-Type": "application/json",
//         ...(token ? { Authorization: `Bearer ${token}` } : {}),
//         ...options.headers,
//     };
//
//     const response = await fetch(url, {
//         ...options,
//         headers,
//     });
//
//     if (!response.ok) {
//         if (response.status === 401) {
//             alert("세션이 만료되었습니다. 다시 로그인하세요.");
//             localStorage.removeItem("accessToken");
//             // window.location.href = "/";
//             return redirect("/");
//         }
//         throw new Error("API 요청 실패");
//     }
//
//     // JSON인지, TEXT인지 확인 후 적절한 방식으로 응답 처리
//     const contentType = response.headers.get("Content-Type");
//     if (contentType?.includes("application/json")) {
//         return response.json(); // JSON 반환
//     } else {
//         return response.text(); // String 반환
//     }
//     // return response.text();
// }
