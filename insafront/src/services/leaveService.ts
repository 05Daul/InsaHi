import axios from 'axios';
import {AnnualLeaveDTO, AnnualLeaveRequestDTO, PageResponseDTO} from '@/type/leave';
import {API_BASE_URL_Leave} from "@/api/api_base_url";

// const API_BASE = 'http://127.0.0.1:1006/leave';

// 휴가 신청
export const submitLeave = async (data: AnnualLeaveRequestDTO): Promise<string> => {
  const res = await axios.post<string>(`${API_BASE_URL_Leave}/submit`, data);
  return res.data;

};

// 남은 연차 조회
export const getRemainingLeave = async (employeeId: string): Promise<AnnualLeaveDTO> => {
  const res = await axios.get<AnnualLeaveDTO>(`${API_BASE_URL_Leave}/remaining/${employeeId}`);
  return res.data;
};

// 직원 휴가 신청 내역 조회 (상태별)
export const getLeaveUsageByEmployeeId = async (employeeId: string, status: string): Promise<AnnualLeaveRequestDTO[]> => {
  const res = await axios.get<AnnualLeaveRequestDTO[]>(`${API_BASE_URL_Leave}/getmyleave/${employeeId}/${status}`);
  return res.data;
};

export const getLeaveUsageByEmployeeIdWithPagination = async (
    employeeId: string,
    status: string,
    page: number,
    size: number
): Promise<PageResponseDTO<AnnualLeaveRequestDTO>> => {
  const res = await axios.get<PageResponseDTO<AnnualLeaveRequestDTO>>(
      `${API_BASE_URL_Leave}/getmyleave/${employeeId}/${status}?page=${page}&size=${size}&sort=createDate,desc`
  );
  return {
    content: res.data.content,
    totalPages: res.data.totalPages,
    totalElements: res.data.totalElements,
    number: res.data.number,
    size: res.data.size,
  };
};

// 회사 전체 휴가 신청 내역 조회 (관리자)
export const getLeaveUsageByCompany = async (companyCode: string, status: string): Promise<AnnualLeaveRequestDTO[]> => {
  const res = await axios.get<AnnualLeaveRequestDTO[]>(`${API_BASE_URL_Leave}/usage/${companyCode}/${status}`);
  return res.data;
};

export const getLeaveUsageByCompanyWithPagination = async (
    companyCode: string,
    status: string,
    page: number,
    size: number
): Promise<{ content: AnnualLeaveRequestDTO[], totalPages: number }> => {
  const res = await axios.get<PageResponseDTO<AnnualLeaveRequestDTO>>(
      `${API_BASE_URL_Leave}/usage/${companyCode}/${status}?page=${page}&size=${size}&sort=createDate,desc`
  );
  return {
    content: res.data.content,
    totalPages: res.data.totalPages
  };
};

// 휴가 승인
export const approveLeave = async (data: AnnualLeaveRequestDTO): Promise<string> => {
  const res = await axios.put<string>(`${API_BASE_URL_Leave}/approve`, data);
  return res.data;
};

// 휴가 반려
export const rejectLeave = async (data: AnnualLeaveRequestDTO): Promise<string> => {
  const res = await axios.put<string>(`${API_BASE_URL_Leave}/reject`, data);
  return res.data;
};

// 추가 연차 승인
export const approveAdditionalLeave = async (data: AnnualLeaveRequestDTO): Promise<string> => {
  const res = await axios.put<string>(`${API_BASE_URL_Leave}/additional`, data);
  return res.data;
};

// 휴가 신청 상세 정보 조회
export const getLeaveDetailById = async (id: number): Promise<AnnualLeaveRequestDTO> => {
  const res = await axios.get<AnnualLeaveRequestDTO>(`${API_BASE_URL_Leave}/detail/${id}`);
  return res.data;
};