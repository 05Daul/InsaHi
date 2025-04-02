export async function fetcher<T>(url: string, options?: RequestInit): Promise<T> {
  const token = typeof window !== 'undefined' ? localStorage.getItem('accessToken') : null;
  console.log('📌 fetcher에서 가져온 accessToken:', token);

  const response = await fetch(url, {
    ...options,
    headers: {
      ...(options?.headers || {}),
      'Content-Type': 'application/json',
      ...(token ? {Authorization: `Bearer ${token}`} : {}),
    },
  });

  if (!response.ok) {
    const errorBody = await response.json().catch(() => ({}));
    const message = errorBody.message || 'Something went wrong';
    console.error('API 오류 발생:', errorBody);
    throw new Error(message);
  }

  if (response.status === 204) {
    return {} as T;
  }

  const contentType = response.headers.get('Content-Type');
  if (contentType && contentType.includes('application/json')) {
    return response.json();
  }

  return response.text().then((text) => {
    try {
      return JSON.parse(text) as T;
    } catch {
      return text as unknown as T;
    }
  });
}