// services/approvalService.ts
import accessToken from '@/lib/accessToken';

interface FormDataPayload {
  id: string;
  name: string;
  text: string;
  companyCode: string;
  employeeId: string | null;
  approvers: string[];
  referencedIds: string[];
}

interface Employee {
  employeeId: string;
  name: string;
  companyCode: string;
}

export async function fetchAllEmployees(): Promise<Employee[]> {
  const response = await accessToken.get<Employee[]>('/employee/allEmp');
  console.log('실제 응답 구조:', response.data);
  return response.data;
}

export async function submitApproval(
    formData: FormDataPayload,
    files: File[]
): Promise<number> {
  const formPayload = new FormData();
  formPayload.append('jsonData', JSON.stringify(formData));
  files.forEach(file => formPayload.append('files', file));

  const response = await accessToken.post('/approval/submit', formPayload, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });

  return response.status;
}
