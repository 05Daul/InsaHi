import {useState, useEffect} from 'react';

interface Document {
  id: string;
  name: string;
  text: string;
}

const DocumentsPage = () => {
  const [menu, setMenu] = useState<number>(1);
  const [documents, setDocuments] = useState<Document[]>([]);
  const employeeId = 'E002';

  useEffect(() => {
    const fetchDocuments = async () => {
      const response = await fetch(`http://127.0.0.1:1005/approval/list/${employeeId}/${menu}`);
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
          <select onChange={(e) => setMenu(parseInt(e.target.value))} value={menu}>
            <option value={1}>본인이 작성한 문서</option>
            <option value={2}>결재자로 지정된 문서</option>
            <option value={3}>결재해야 할 문서</option>
          </select>
        </div>
        <div>
          <h2>문서 목록</h2>
          <ul>
            {documents.map((doc) => (
                <li key={doc.id}>
                  <a href={`/approval/file/${doc.id}`}>{doc.name}</a>
                </li>
            ))}
          </ul>
        </div>
      </div>
  );
};

export default DocumentsPage;
