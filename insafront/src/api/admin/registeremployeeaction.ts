import {API_BASE_URL_Employee} from "@/api/api_base_url";

export async function RegisterEmployeeAction(formData: FormData) {
  console.log("서버 액션 실행됨!");
  const url = `${API_BASE_URL_Employee}/insertemployee`;
  const formObject = Object.fromEntries(formData);
  console.log("폼 데이터:", formObject);
  let token = "";
  if (typeof window !== "undefined") {
    token = localStorage.getItem("accessToken") || "";
  }

  const response = await fetch(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "Authorization": `Bearer ${token}`
    },
    body: JSON.stringify(formObject),
  });

  let responseData;
  try {
    responseData = await response.json(); // ✅ JSON 응답이라면 이게 정상 작동
  } catch (e) {
    // ❌ JSON이 아닌 경우 여기로 빠짐
    const text = await response.text();
    console.error("⚠️ 서버 응답이 JSON이 아님:", text);
    responseData = {message: text}; // fallback 응답
  }


  return responseData;//alert("회원가입 완료 !");

}