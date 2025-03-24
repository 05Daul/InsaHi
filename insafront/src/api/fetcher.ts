export async function fetcher<T>(url: string, options?: RequestInit): Promise<T> {
    const response = await fetch(url, {
        ...options,
        headers: {
            'Content-Type': 'application/json',
            ...(options?.headers || {}),
        },
    });

    if (!response.ok) {
        const errorBody = await response.json().catch(() => ({}));
        const message = errorBody.message || 'Something went wrong';
        console.error('API 오류 발생:', errorBody);
        throw new Error(message);
    }

    // 응답 본문이 비어있으면 빈 객체 반환
    if (response.status === 204) {
        return {} as T;
    }

    const contentType = response.headers.get('Content-Type');
    if (contentType && contentType.includes('application/json')) {
        return response.json();
    }

    // JSON이 아닌 경우 텍스트 반환
    return response.text().then((text) => {
        try {
            return JSON.parse(text) as T;
        } catch {
            return text as unknown as T;
        }
    });
}
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
