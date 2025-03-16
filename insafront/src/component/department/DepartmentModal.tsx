import { Department } from '../../type/Employee';


interface DepartmentModalProps {
    department: Department;
    onClose: () => void;
}

const DepartmentModal = ({ department, onClose }: DepartmentModalProps) => {
    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
            <div className="bg-white p-6 rounded shadow-lg w-96">
                <h2 className="text-xl font-bold mb-4">{department.departmentName}</h2>
                <p>직원 수: {department.employees?.length || 0}</p>

                {/* 닫기 버튼 */}
                <button
                    onClick={onClose}
                    className="mt-4 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
                >
                    닫기
                </button>
            </div>
        </div>
    );
};

export default DepartmentModal;
