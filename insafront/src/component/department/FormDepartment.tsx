// src/component/department/FormDepartment.tsx
import {useState} from "react";

interface Props {
  onSubmit: (formData: { departmentName: string }) => void;
  loading: boolean;
}

export default function FormDepartment({onSubmit, loading}: Props) {
  const [departmentName, setDepartmentName] = useState("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit({departmentName});
    setDepartmentName("");
  };

  return (
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium">부서명</label>
          <input
              type="text"
              value={departmentName}
              onChange={(e) => setDepartmentName(e.target.value)}
              required
              className="w-full border rounded p-2"
          />
        </div>
        <button
            type="submit"
            disabled={loading}
            className={`w-full px-4 py-2 rounded text-white ${
                loading ? "bg-gray-400" : "bg-blue-600 hover:bg-blue-700"
            }`}
        >
          {loading ? "등록 중..." : "부서 등록"}
        </button>
      </form>
  );
}
