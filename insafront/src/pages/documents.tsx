import {useState, useEffect} from 'react';

interface Document {
  id: string;
  title: string;
  content: string;
}

const DocumentsPage = () => {
  const [menu, setMenu] = useState<'owned' | 'assigned' | 'pending'>('owned');
  const [documents, setDocuments] = useState<Document[]>([]);
  const employeeId = 'employee1'; // 예시로 지정한 직원 ID

  useEffect(() => {
    const fetchDocuments = async () => {
      const response = await fetch(`http://127.0.0.1:1005/approve/list?employeeId=${employeeId}&menu=${menu}`);
      const data = await response.json();
      setDocuments(data);
    };

    fetchDocuments();
  }, [menu]);

  return (
      <div>
        <h1>결재 문서 목록</h1>
        <div>
          <label>조회할 문서 종류</label>
          <select onChange={(e) => setMenu(e.target.value as 'owned' | 'assigned' | 'pending')}
                  value={menu}>
            <option value="owned">본인이 작성한 문서</option>
            <option value="assigned">결재자로 지정된 문서</option>
            <option value="pending">결재해야 할 문서</option>
          </select>
        </div>
        <div>
          <h2>문서 목록</h2>
          <ul>
            {documents.map((doc) => (
                <li key={doc.id}>
                  <a href={`/file/${doc.id}`}>{doc.title}</a>
                </li>
            ))}
          </ul>
        </div>
      </div>
  );
};

export default DocumentsPage;
