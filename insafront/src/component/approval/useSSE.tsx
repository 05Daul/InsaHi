// hooks/useSSE.ts
import {useEffect} from 'react';

const useSSE = (employeeId: string | null, onMessage: (msg: string) => void) => {
  useEffect(() => {
    if (!employeeId) return;

    const eventSource = new EventSource(`http://127.0.0.1:1005/api/sse/subscribe/${employeeId}`);

    eventSource.onmessage = (event) => {
      console.log('기본 메세지: ', event.data);
    };

    eventSource.addEventListener('approval-update', (event) => {
      console.log('결재 알림 수신:', event.data);
      onMessage(event.data);
    });

    eventSource.addEventListener('broadcast', (event) => {
      console.log('브로드캐스트 알림:', event.data);
      onMessage(event.data);
    });

    eventSource.onerror = (error) => {
      console.error('SSE 연결 오류:', error);
      eventSource.close();
    };

    return () => {
      eventSource.close();
    };
  }, [employeeId]);
};

export default useSSE;
