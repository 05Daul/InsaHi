// src/services/departmentService.ts

import axios from "axios";

const BASE_URL = "http://127.0.0.1:1006";

export const fetchDepartments = async () => {
  const response = await axios.get(`${BASE_URL}/department/list`);
  return response.data;
};

export const createDepartment = async (companyCode: string, payload: any) => {
  const response = await axios.post(`${BASE_URL}/department/create`, payload);
  return response.data;
};
