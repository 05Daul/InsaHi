// src/pages/setting/setdepartment.tsx

import React, {useEffect, useState} from "react";
import {getParentDepartments, submitDepartment} from "@/services/createDepartmentAction";
import {DepartmentListForCreate} from "@/type/DepartmentListForCreate";
import FormDepartment from "@/component/department/FormDepartment";
import SelectDepartment from "@/component/department/SelectDepartment";

const useLocalStorage = (key: string, defaultValue = "") => {
  const [value, setValue] = useState<string>(defaultValue);
  useEffect(() => {
    setValue(localStorage.getItem(key) ?? defaultValue);
  }, [key]);
  return value;
};

export default function CreateDepartmentPage() {
  const [parentDepartments, setParentDepartments] = useState<DepartmentListForCreate[]>([]);
  const [selectedParentId, setSelectedParentId] = useState("");
  const [loading, setLoading] = useState(false);

  const companyCode = useLocalStorage("companyCode", "");
  useEffect(() => {
    if (parentDepartments.length > 0 && selectedParentId === "") {
      setSelectedParentId(parentDepartments[0].departmentId); // 첫 번째 부서 자동 선택
    }
  }, [parentDepartments, selectedParentId]);

  useEffect(() => {
    if (companyCode) {
      getParentDepartments(companyCode)
      .then(setParentDepartments)
      .catch((err) => {
        console.error("부서 목록 조회 실패:", err);
        alert("부서 목록을 불러오는 데 실패했습니다.");
      });
    }
  }, [companyCode]);

  useEffect(() => {
    if (parentDepartments.length > 0 && selectedParentId === "") {
      setSelectedParentId(parentDepartments[0].departmentId); // 첫 번째 부서 자동 선택
    }
  }, [parentDepartments, selectedParentId]);

  const handleSubmit = async ({departmentName}: { departmentName: string }) => {
    if (!companyCode) return alert("회사 코드가 없습니다.");

    const payload = {
      departmentName,
      parentDepartmentId: selectedParentId,
    };

    try {
      setLoading(true);
      await submitDepartment(companyCode, payload);
      alert("부서가 성공적으로 생성되었습니다!");
      setSelectedParentId("");
      const updatedList = await getParentDepartments(companyCode);
      setParentDepartments(updatedList);
    } catch (error) {
      alert("부서 생성에 실패했습니다. error: " + error);
    } finally {
      setLoading(false);
    }
  };

  return (
      <div className="p-6 max-w-md mx-auto bg-white rounded shadow">
        <h2 className="text-xl font-bold mb-4">부서 등록</h2>
        <div className="mt-4">
          <label className="block text-sm font-medium">상위 부서 (선택)</label>

          <SelectDepartment
              departments={parentDepartments}
              selected={selectedParentId}
              onChange={setSelectedParentId}

          />
        </div>
        <FormDepartment onSubmit={handleSubmit} loading={loading}/>
      </div>
  );
}
