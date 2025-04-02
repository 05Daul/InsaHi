// src/services/createDepartmentAction.ts

import {
  ActionBasedOrganizationChartForCreateDTO,
  DepartmentListForCreate
} from "@/type/DepartmentListForCreate";
import accessToken from "@/lib/accessToken";

const BASE_URL = "http://127.0.0.1:1006/department";

// 상위 부서 리스트 조회
/*export const getParentDepartments = async (
    companyCode: string
): Promise<DepartmentListForCreate> => {
  const res = await axios.get<DepartmentListForCreate>(`${BASE_URL}/${companyCode}/list`);
  return res.data;
};*/

// 서비스에서 배열로 반환되도록 수정
export const getParentDepartments = async (
    companyCode: string
): Promise<DepartmentListForCreate[]> => {
  const res = await accessToken.get<DepartmentListForCreate[]>(
      `${BASE_URL}/${companyCode}/list`
  );
  console.log(res.data)
  return res.data;
};


// POST: 부서 생성 요청
export const submitDepartment = async (
    companyCode: string,
    payload: {
      departmentName: string;
      parentDepartmentId: string;
    }
): Promise<ActionBasedOrganizationChartForCreateDTO> => {
  const res = await accessToken.post<ActionBasedOrganizationChartForCreateDTO>(
      `${BASE_URL}/${companyCode}/create`,
      payload
  );
  console.log("resresresresresrtesres" + payload.parentDepartmentId)
  return res.data;
};
