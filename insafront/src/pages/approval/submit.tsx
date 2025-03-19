import {useState, ChangeEvent, FormEvent} from 'react';

interface FormData {
  id: string;
  name: string;
  text: string;
  approvers: string;
  referencedIds: string;
}

const SubmitPage = () => {
  const [formData, setFormData] = useState<FormData>({
    id: '',
    name: '',
    text: '',
    approvers: '',
    referencedIds: ''
  });


  // 📌 일반 입력값 핸들러
  const handleInputChange = (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const {name, value} = e.target;
    setFormData((prevData) => ({...prevData, [name]: value}));
  };

  //  파일 첨부 핸들러
  const [files, setFiles] = useState<File[]>([]);

  const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      const fileList = Array.from(e.target.files);
      setFiles(fileList);
    }
  };

  // 📌 최종 상신 핸들러 (파일 + JSON)
  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();

    const formPayload = new FormData();

    // 1) JSON 데이터
    const jsonData = {
      id: formData.id,
      name: formData.name,
      text: formData.text,
      companyCode: 'C001',
      employeeId: 'E001',
      approvers: formData.approvers.split(',').map(item => item.trim()),
      referencedIds: formData.referencedIds.split(',').map(item => item.trim())
    };
    formPayload.append('jsonData', JSON.stringify(jsonData));

    // 2) 파일 데이터
    files.forEach(file => {
      formPayload.append('files', file); // 여기 이름이 files 이어야 함!
    });
    console.log(formPayload.get('jsonData'));
    console.log(formPayload.get('files'));

    try {
      for (let pair of formPayload.entries()) {
        console.log(pair[0] + ',++++++++<, ' + pair[1]);
      }
      const response = await fetch('http://127.0.0.1:1005/approval/submit', {
        method: 'POST',
        body: formPayload // 절대 headers 안 건드림!
      });

      if (response.ok) {
        alert('성공!');
      } else {
        alert(`실패! 상태코드: ${response.status}`);
      }
    } catch (error) {
      console.error('에러:', error);
    }
  };

  return (
      <div>
        <h1>결재 문서 상신 (파일 포함)</h1>
        <form onSubmit={handleSubmit}>
          <div>
            <label>제목</label>
            <input
                type="text"
                name="name"
                value={formData.name}
                onChange={handleInputChange}
                required
            />
          </div>

          <div>
            <label>내용</label>
            <textarea
                name="text"
                value={formData.text}
                onChange={handleInputChange}
                required
            />
          </div>

          <div>
            <label>결재자 (쉼표로 구분)</label>
            <input
                type="text"
                name="approvers"
                value={formData.approvers}
                onChange={handleInputChange}
                placeholder="예: user1,user2"
            />
          </div>

          <div>
            <label>참조자 (쉼표로 구분)</label>
            <input
                type="text"
                name="referencedIds"
                value={formData.referencedIds}
                onChange={handleInputChange}
                placeholder="예: ref1,ref2"
            />
          </div>

          <div>
            <label>첨부파일</label>
            <input
                type="file"
                multiple  // 여러 파일 선택 허용
                onChange={handleFileChange}
            />
          </div>

          <button type="submit">문서 상신</button>
        </form>
      </div>
  );
};

export default SubmitPage;
