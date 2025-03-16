import {useState, ChangeEvent, FormEvent} from 'react';

interface FormData {
  title: string;
  content: string;
  approvers: string;
  referrers: string;
}

const SubmitPage = () => {
  const [formData, setFormData] = useState<FormData>({
    title: '',
    content: '',
    approvers: '',
    referrers: ''
  });

  const handleInputChange = (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const {name, value} = e.target;
    setFormData((prevData) => ({...prevData, [name]: value}));
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();

    const requestData = {
      employeeId: 'employee1',  // 예시로 지정한 직원 ID
      companyCode: 'company123', // 예시로 지정한 회사 코드
      dto: {title: formData.title, content: formData.content},
      approvers: formData.approvers.split(','),
      referrers: formData.referrers.split(',')
    };

    const response = await fetch('http://127.0.0.1:1005/approve/submit', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(requestData)
    });

    if (response.ok) {
      alert('결재 문서가 상신되었습니다.');
    } else {
      alert('상신 실패');
    }
  };

  return (
      <div>
        <h1>결재 문서 제출</h1>
        <form onSubmit={handleSubmit}>
          <div>
            <label>제목</label>
            <input
                type="text"
                name="title"
                value={formData.title}
                onChange={handleInputChange}
            />
          </div>
          <div>
            <label>내용</label>
            <textarea
                name="content"
                value={formData.content}
                onChange={handleInputChange}
            />
          </div>
          <div>
            <label>결재자 (쉼표로 구분)</label>
            <input
                type="text"
                name="approvers"
                value={formData.approvers}
                onChange={handleInputChange}
            />
          </div>
          <div>
            <label>참조자 (쉼표로 구분)</label>
            <input
                type="text"
                name="referrers"
                value={formData.referrers}
                onChange={handleInputChange}
            />
          </div>
          <button type="submit">상신하기</button>
        </form>
      </div>
  );
};

export default SubmitPage;
