import {ATTENDANCE_API_URL} from '@/config/env';
import {fetcher} from "@/api/fetcher";

export const attendanceFetcher = <T>(endpoint: string, options?: RequestInit): Promise<T> => {
  return fetcher<T>(`${ATTENDANCE_API_URL}${endpoint}`, options);
};