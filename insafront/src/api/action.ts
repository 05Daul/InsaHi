"use server"


//Company && Admin 회원가입 action

import {API_BASE_URL_Company, API_BASE_URL_Employee} from "@/api/api_base_url";

export async function signup(formData: FormData) {
  console.log("서버 액션 실행됨!");

  const formObject = Object.fromEntries(formData);
  console.log("폼 데이터:", formObject);
  const response = await fetch(`${API_BASE_URL_Company}/signup`, {
    method: "POST",
    headers: {"Content-Type": "application/json"},
    body: JSON.stringify(formObject),
  });

  const responseData = await response.json();
  console.log("서버 응답 action.ts:", responseData);

  if (responseData.redirectUrl) {
    window.location.href = responseData.redirectUrl; // 로그인 페이지로 이동
  }


  return responseData;//alert("회원가입 완료 !");

}

export async function mail(employeeId: string) {
  const response = await fetch(`${API_BASE_URL_Company}/email/${employeeId}`, {
    method: "POST",
    headers: {"Content-Type": "application/json"},
  });

  if (!response.ok) {
    throw new Error("이메일 전송 실패");
  }


  return await response.json();
}

//login action
export async function login(loginData: FormData) {
  console.log("서버 액션 실행됨!");

  const formObject = Object.fromEntries(loginData);
  console.log("폼 데이터:", formObject);

  const res = await fetch(`${API_BASE_URL_Employee}/login`, {
    method: "POST",
    headers: {
      "Accept": "application/json",
      "Content-Type": "application/json",
      "Header": ""
    },
    body: JSON.stringify(formObject),
    cache: "no-store", //항상 최신 데이터를 가져오기 위해서 처리
  });

  if (!res.ok) {
    throw new Error(`로그인 실패: ${res.status}`);
  }

  const token = await res.json();
  console.log("✅ 로그인 성공, 받은 토큰:", token);

  // 받은 토큰을 저장 (로컬스토리지)
  if (typeof window !== "undefined") {
    localStorage.setItem("accessToken", token);
  }

  return token; // 토큰 반환

}