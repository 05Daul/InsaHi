import React, {useEffect, useRef, useState} from "react";
import ContextMenu from "./ContextMenu";
import {API_BASE_URL_Chat} from "@/api/api_base_url";

interface Message {
  chatId?: string;
  name: string;
  roomId: string;
  content: string;
  createdAt: string;
  read: boolean,
  readBy?: string[];
  deleted?: boolean;
}

interface ChatAreaProps {
  currentUserName: string | null;
  currentRoomId: string | null;
  stompClient: any | null;
  participantCount: number;
  onNewMessageArrived?: () => void;
  onMessagesRead?: () => void;
}

const ChatArea: React.FC<ChatAreaProps> = ({
                                             currentUserName,
                                             currentRoomId,
                                             stompClient,
                                             onNewMessageArrived, // 추가
                                             onMessagesRead
                                           }) => {
  const [participantCount, setParticipantCount] = useState<number>(1);
  const [messages, setMessages] = useState<Message[]>([]);
  const [contextMenu, setContextMenu] = useState<{
    x: number;
    y: number;
    visible: boolean;
    chatId: string | null
  }>({
    x: 0,
    y: 0,
    visible: false,
    chatId: null,
  });
  const messageRef = useRef<HTMLInputElement>(null);
  const bottomRef = useRef<HTMLDivElement | null>(null);
// 날짜가 다른지 확인
  const isDifferentDate = (date1: string, date2: string) => {
    return new Date(date1).toDateString() !== new Date(date2).toDateString();
  };

// 날짜 포맷 예: 2025년 3월 28일
  const formatDateHeader = (createdAt: string) => {
    const date = new Date(createdAt);
    return `${date.getFullYear()}년 ${date.getMonth() + 1}월 ${date.getDate()}일`;
  };

// 시:분 포맷 예: 오후 3:21
  const formatTime = (timestamp: string) => {
    const date = new Date(timestamp);
    const hours = date.getHours();
    const minutes = date.getMinutes();
    const isAm = hours < 12;
    const formattedHours = hours % 12 || 12;
    return `${isAm ? "오전" : "오후"} ${formattedHours}:${minutes.toString().padStart(2, "0")}`;
  };

  // ✅ 채팅방 변경 시 메시지 불러오기
  useEffect(() => {
    if (!currentRoomId) return;

    fetch(`${API_BASE_URL_Chat}/messages/${currentRoomId}`)
    .then((res) => res.json())
    .then((data) => {
      console.log("📥 메시지 로드 성공:", data);
      setMessages(data);
    })
    .catch((err) => console.error("메시지 불러오기 실패:", err));
  }, [currentRoomId]);

  // ✅ WebSocket 메시지 구독
  useEffect(() => {
    if (!stompClient || !currentRoomId) return;

    // stompClient.connected === true 인지 먼저 확인
    if (!stompClient.connected) {
      console.warn("아직 WebSocket 연결되지 않음");
      return;
    }

    const subscription = stompClient.subscribe(`/topic/messages/${currentRoomId}`, (message: any) => {
      const newMessage = JSON.parse(message.body);
      console.log("📨 실시간 메시지 도착:", newMessage);

      setMessages((prev) => {
        const filtered = prev.filter(
            (msg) =>
                !(
                    msg.content === newMessage.content &&
                    msg.name === newMessage.name &&
                    msg.roomId === newMessage.roomId &&
                    !msg.chatId // chatId 없는 건 임시로 간주
                )
        );
        return [...filtered, newMessage];
      });
      // 새 메시지 들어온 사실을 부모에게 알림
      if (onNewMessageArrived) {
        onNewMessageArrived();
      }
    });

    return () => subscription.unsubscribe();
  }, [stompClient, currentRoomId]);

  // ✅ 메시지 보내기
  async function sendMessage() {
    if (!stompClient || !currentRoomId || !messageRef.current) return;
    const message = messageRef.current.value.trim();
    if (!message) return;

    const tempMessage: Message = {
      chatId: undefined,
      name: currentUserName!,
      content: message,
      roomId: currentRoomId,
      createdAt: new Date().toISOString(),
      read: false,
      readBy: [currentUserName!],
      deleted: false,
    };

    // 먼저 뷰에 보여줌 (메시지 삭제 실시간 때문에)
    setMessages((prev) => [...prev, tempMessage]);

    try {
      const formData = new FormData();
      formData.append("name", currentUserName!);
      formData.append("roomId", currentRoomId);
      formData.append("content", message);

      const res = await fetch(`${API_BASE_URL_Chat}/send`, {
        method: "POST",
        body: formData,
      });

      if (!res.ok) {
        const errText = await res.text();
        throw new Error("전송 실패: " + errText);
      }

      messageRef.current.value = ""; // 입력창 비우기
    } catch (error) {
      console.error("메시지 전송 오류:", error);
    }
  }

  //자동 스크롤
  useEffect(() => {
    bottomRef.current?.scrollIntoView({behavior: 'smooth'});
  }, [messages]);

  //우클릭 메뉴처리
  const handleContextMenu = (event: React.MouseEvent, chatId: string) => {
    event.preventDefault();
    setContextMenu({
      x: event.clientX + window.scrollX,
      y: event.clientY + window.scrollY,
      visible: true,
      chatId,
    });
  };

// 삭제 버튼 눌렀을 때
  const handleDelete = () => {
    if (contextMenu.chatId) {
      handleDeleteMessage(contextMenu.chatId);
    }
    setContextMenu({...contextMenu, visible: false});
  };

  // ✅ 메시지 삭제
  const handleDeleteMessage = async (chatId: string) => {
    try {
      const res = await fetch(`${API_BASE_URL_Chat}/messages/${chatId}`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (!res.ok) {
        throw new Error("메시지 삭제 실패");
      }

      setMessages((prev) => prev.map((msg) =>
          msg.chatId === chatId ? {...msg, deleted: true} : msg));
    } catch (err) {
      console.error("삭제 오류:", err);
    }
  };
  //메시지 읽음 표시 구현
  useEffect(() => {
    if (!currentRoomId || !currentUserName) return;

    const markAsRead = async () => {
      try {
        await fetch(`${API_BASE_URL_Chat}/rooms/${currentRoomId}/read?name=${currentUserName}`, {
          method: "POST",
          headers: {
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
            "Content-Type": "application/json",
          },
          body: JSON.stringify({reader: currentUserName}),

        });
        // 읽음 처리 끝났다고 부모에 알림
        if (onMessagesRead) {
          onMessagesRead();
        }
      } catch (err) {
        console.error("읽음 처리 실패", err);
      }
    };

    markAsRead();
  }, [messages, currentUserName, currentRoomId]);

  //participantCount를 사용해서 안읽음 표시 패치
  useEffect(() => {
    if (!currentRoomId) return;

    fetch(`${API_BASE_URL_Chat}/rooms/${currentRoomId}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        "Content-Type": "application/json"
      }
    })
    .then((res) => {
      if (!res.ok) throw new Error("방 정보 불러오기 실패");
      return res.json();
    })
    .then((data) => {
      console.log("👤 참여자 수 가져온 data:", data);
      setParticipantCount(data.name?.length || 1);
    })
    .catch((err) => {
      console.error("방 참여자 수 로드 실패", err);
    });
  }, [currentRoomId]);


  return (
      <div
          style={{
            position: "absolute",
            top: "60px", // 헤더 높이
            left: "25%", // roomList 만큼 밀기
            width: "75%",
            height: "calc(100vh - 60px - 70px)", // header + input 영역 제외
            overflowY: "auto",
            padding: "10px",
            boxSizing: "border-box",
            display: "flex",
            flexDirection: "column",
          }}
      >
        {messages.map((msg, index) => {
          const prev = messages[index - 1];
          const showDate = index === 0 || (prev && isDifferentDate(msg.createdAt, prev.createdAt));

          return (
              <React.Fragment key={msg.chatId || `${msg.name}-${msg.content}-${msg.roomId}`}>
                {showDate && (
                    <div
                        style={{
                          textAlign: "center",
                          color: "#888",
                          fontSize: "13px",
                          margin: "16px 0 8px",
                        }}
                    >
                      {formatDateHeader(msg.createdAt)}
                    </div>
                )}

                {/* 바깥쪽 메시지 박스 (정렬용) */}
                <div style={{
                  display: "flex",
                  justifyContent: msg.name === currentUserName ? "flex-end" : "flex-start",
                  marginBottom: "10px",
                }}>
                  {/* 말풍선 박스 */}
                  <div
                      onContextMenu={(e) => handleContextMenu(e, msg.chatId ?? "")}
                      style={{
                        backgroundColor: msg.name === currentUserName ? "#d4f8c4" : "#f1f1f1",
                        padding: "10px",
                        borderRadius: "10px",
                        maxWidth: "60%",
                        wordBreak: "break-word",
                        color: "#000",
                        boxShadow: "0 1px 3px rgba(0,0,0,0.1)",
                        alignSelf: msg.name === currentUserName ? "flex-end" : "flex-start",
                      }}
                  >
                    <strong>{msg.name}</strong>: {msg.deleted ? <i>삭제된 메시지입니다.</i> : msg.content}
                    {/*// 말풍선 시간 표시*/}
                    <div style={{
                      fontSize: "10px",
                      color: "#888",
                      marginTop: "4px",
                      textAlign: "right"
                    }}>
                      {formatTime(msg.createdAt)}
                    </div>

                    {/*//안읽은 사람 수 표시 */}
                    {(() => {
                      const total = participantCount || 0;
                      const readBy = msg.readBy ?? [];
                      const unreadCount = total - readBy.length;

                      // 참여자가 1 이하이거나, 모든 사람이 읽은 상태면 표시 안함
                      if (total <= 1 || unreadCount <= 0) return null;

                      return (
                          <div
                              style={{
                                fontSize: "10px",
                                color: "red",
                                marginTop: "4px",
                                textAlign: "left",
                              }}
                          >
                            {unreadCount}명 안읽음
                          </div>
                      );
                    })()}
                  </div>
                </div>
              </React.Fragment>
          );
        })}
        <div ref={bottomRef}/>

        {/* 메시지 입력창 */}
        <div
            style={{
              position: "fixed",
              bottom: 0,
              left: "25%",
              width: "75%",
              backgroundColor: "#fff",
              padding: "10px",
              display: "flex",
              borderTop: "1px solid #ccc",
              boxShadow: "0 -2px 5px rgba(0,0,0,0.1)",
            }}
        >
          <input
              type="text"
              ref={messageRef}
              placeholder="메시지를 입력하세요"
              onKeyDown={(e) => {
                if (e.key === "Enter") sendMessage();
              }}
              style={{flex: 1, padding: "5px"}}
          />
          <button onClick={sendMessage} style={{marginLeft: "5px"}}>
            전송
          </button>
        </div>

        {/* 우클릭 삭제 메뉴 */}
        {contextMenu.visible && (
            <ContextMenu
                x={contextMenu.x}
                y={contextMenu.y}
                visible={contextMenu.visible}
                onClose={() => setContextMenu({...contextMenu, visible: false})}
                onDelete={handleDelete}
            />
        )}
      </div>
  );
};

export default ChatArea;