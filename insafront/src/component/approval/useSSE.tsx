// component/approval/useSSE.tsx
import {useEffect, useRef} from 'react';

const eventSourceMap = new Map<string, EventSource>(); // employeeId별 싱글톤 유지

const useSSE = (employeeId: string | null, onMessage: (msg: string) => void) => {
  const handlerRef = useRef(onMessage);
  handlerRef.current = onMessage;

  useEffect(() => {
    if (typeof window === 'undefined') return; // SSR 보호
    if (!employeeId || employeeId === 'defaultId') return;

    // 이미 연결되어 있다면 새로 만들지 않음
    if (eventSourceMap.has(employeeId)) return;

    const eventSource = new EventSource(`http://127.0.0.1:1006/approval/sse/subscribe/${employeeId}`);
    eventSourceMap.set(employeeId, eventSource);

    const handleEvent = (event: MessageEvent) => {
      console.log('수신된 이벤트:', event.data);
      handlerRef.current(event.data); // 최신 핸들러 사용
    };

    eventSource.addEventListener('approval-update', handleEvent);
    eventSource.addEventListener('broadcast', handleEvent);

    eventSource.onerror = (error) => {
      console.error('SSE 연결 오류:', error);
      eventSource.close();
      eventSourceMap.delete(employeeId);
    };

    return () => {
      // 언마운트 시 SSE 종료 및 map에서 제거
      eventSource.close();
      eventSourceMap.delete(employeeId);
    };
  }, [employeeId]);
};

export default useSSE;
