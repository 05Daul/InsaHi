import axios from "axios";

const accessToken = axios.create({
  baseURL: "http://127.0.0.1:1006", // 백엔드 주소
  withCredentials: false, // 필요 시 true
});

accessToken.interceptors.request.use((config) => {
  const token = localStorage.getItem("accessToken");
  if (token) {
    config.headers = config.headers ?? {}; // 👈 headers가 undefined면 새 객체로 초기화
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default accessToken;
