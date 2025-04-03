// src/services/departmentService.ts

import axios from "axios";
import {API_BASE_URL} from "@/api/api_base_url";

const BASE_URL = `${API_BASE_URL}`;

export const fetchDepartments = async () => {
  const response = await axios.get(`${BASE_URL}/department/list`);
  return response.data;
};

export const createDepartment = async (companyCode: string, payload: any) => {
  const response = await axios.post(`${BASE_URL}/department/create`, payload);
  return response.data;
};
