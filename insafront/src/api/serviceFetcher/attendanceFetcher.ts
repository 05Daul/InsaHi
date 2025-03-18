import { ATTENDANCE_API_URL } from '@/config/env';
import {fetcher} from "@/api/fetcher";

export const attendanceFetcher = <T>(endpoint: string, options?: RequestInit) => {
    const url = `${ATTENDANCE_API_URL}${endpoint}`;
    return fetcher<T>(url, options);
};