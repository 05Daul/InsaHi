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
        console.error('API 오류 발생:', errorBody);  // 에러 본문 로그 추가
        throw new Error(message);
    }

    return response.json();
}