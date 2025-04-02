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

export async function fetchAllEmployees(companyCode: string): Promise<{
  employeeId: string;
  name: string
}[]> {
  const response = await accessToken.get('/employee/all');
  const all = response.data;
  return all.filter((user: any) => user.companyCode === companyCode);
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
