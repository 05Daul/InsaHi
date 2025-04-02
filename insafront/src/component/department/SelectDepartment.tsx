// src/component/department/SelectDepartment.tsx
import React from "react";
import {DepartmentListForCreate} from "@/type/DepartmentListForCreate";
import styles from "@/styles/setting/setdepartment.module.css"

interface Props {
  departments: DepartmentListForCreate[];
  selected: string;
  onChange: (value: string) => void;
}

const renderOptions = (
    departments: DepartmentListForCreate[],
    level: number = 0 // ← 내부에서만 쓰는 들여쓰기 용도
): React.ReactNode[] => {
  return departments.flatMap((dept) => {
    const indent = "  ".repeat(level);// 
    const currentOption = (
        <option key={dept.departmentId} value={dept.departmentId}>
          {indent}📁 {dept.departmentName}
        </option>
    );
    const childOptions = dept.subDepartments
        ? renderOptions(dept.subDepartments, level + 1)
        : [];
    return [currentOption, ...childOptions];
  });
};

export default function SelectDepartment({departments, selected, onChange}: Props) {
  return (
      <div>
        <select
            value={selected}
            onChange={(e) => {
              // console.log("🟡 선택한 부서 ID:", e.target.value); // ✅ 로그 찍기
              onChange(e.target.value);
            }}
            className={styles.input}
        >
          {renderOptions(departments)}
        </select>
      </div>
  );
}
