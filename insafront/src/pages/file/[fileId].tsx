import {useRouter} from 'next/router';
import {useState, useEffect} from 'react';

interface File {
  id: string;
  title: string;
  content: string;
}

const FilePage = () => {
  const router = useRouter();
  const {fileId} = router.query;
  const [file, setFile] = useState<File | null>(null);

  useEffect(() => {
    if (!fileId) return;

    const fetchFileDetails = async () => {
      const response = await fetch(`http://127.0.0.1:1005/approve?file=${fileId}`);
      const data = await response.json();
      setFile(data);
    };

    fetchFileDetails();
  }, [fileId]);

  if (!file) {
    return <div>Loading...</div>;
  }

  return (
      <div>
        <h1>결재 문서 상세</h1>
        <p>문서 제목: {file.title}</p>
        <p>문서 내용: {file.content}</p>
      </div>
  );
};

export default FilePage;
