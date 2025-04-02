import React, {useEffect, useState} from "react";

interface ChatRoom {
  roomId: string;
  roomName: string;
  name: string[];
  createdAt: string;
  creatorName: string;
  unreadCount?:number ; //안읽은 메시지 수
  lastMessage: string; //마지막 메시지 미리보기
}

interface ChatRoomListProps {
  currentUserName: string | null;
  stompClient: any;
  onSelectRoom: (roomId: string, name: string[]) => void;
  onCreateRoom: () => void;
  reloadRooms: boolean;
  onViewRoomInfo: (room: {
    roomId: string;
    roomName: string;
    name: string[];
    createdAt: string;
    creatorName: string;
    lastMessage: string;
  }) => void;
  selectedRoomId: string | null;
}

const ChatRoomList: React.FC<ChatRoomListProps> = ({
                                                     currentUserName,
                                                     stompClient,
                                                     onSelectRoom,
                                                     reloadRooms,
                                                     onCreateRoom,
                                                     onViewRoomInfo,
                                                     selectedRoomId
                                                   }) => {
  const [rooms, setRooms] = useState<ChatRoom[]>([]);
  const [roomInfoModalVisible, setRoomInfoModalVisible] = useState(false);
  const [selectedRoomInfo, setSelectedRoomInfo] = useState<any>(null);
  const [contextMenu, setContextMenu] = useState<{
    visible: boolean;
    x: number;
    y: number;
    room: ChatRoom | null
  }>({
    visible: false,
    x: 0,
    y: 0,
    room: null,
  });

  useEffect(() => {
    if (!currentUserName) return;
    fetchRooms();
  }, [currentUserName, reloadRooms]);

  useEffect(() => {
    const handleClickOutside = () => {
      if (contextMenu.visible) {
        setContextMenu({ ...contextMenu, visible: false });
      }
    };
    document.addEventListener("click", handleClickOutside);
    return () => document.removeEventListener("click", handleClickOutside);
  }, [contextMenu]);

  function fetchRooms() {
    const token = localStorage.getItem("accessToken");
    if (!token) {
      console.error("❌ 토큰 없음");
      return;
    }

    fetch(`http://127.0.0.1:1006/chat/rooms/member/${currentUserName}`, {
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    })
    .then((res) => {
      if (!res.ok) throw new Error("❌ 방 목록 요청 실패");
      return res.json();
    })
    .then((data) => {
      console.log("✅ 방 목록:", data);
      setRooms(data);
    })
    .catch((err) => console.error("방 목록 오류", err));
  }

  function handleRoomClick(roomId: string, name: string[]) {
    onSelectRoom(roomId,name);
  }

  function handleContextMenu(e: React.MouseEvent, room: ChatRoom) {
    e.preventDefault();
    setContextMenu({visible: true, x: e.pageX, y: e.pageY, room});

    const token = localStorage.getItem("accessToken");
    if (!token) return;

    fetch(`http://127.0.0.1:1006/chat/rooms/${room.roomId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    })
    .then((res) => res.json())
    .then((data) => {
      setSelectedRoomInfo({
        ...data,
        name: data.name ?? [],
      });
      setRoomInfoModalVisible(true); // 모달 열기
    })
    .catch((err) => {
      console.error("방 정보 불러오기 실패", err);
    });
  }


  function handleLeaveRoom() {
    if (!contextMenu.room) return;
    fetch(`http://127.0.0.1:1006/chat/rooms/${contextMenu.room.roomId}/members/${currentUserName}`, {
      method: "DELETE",
    })
    .then(() => {
      alert("방에서 나갔습니다.");
      fetchRooms();
    })
    .catch(() => alert("방 나가기 실패"));

    setContextMenu({visible: false, x: 0, y: 0, room: null});
  }

  const containerStyle = {
    width: "25%",
    backgroundColor: "#e0e9fa", // 🔥 파란색 톤 적용
    borderRight: "1px solid #ccc",
    position: "fixed",
    top: "60px",
    left: "0",
    bottom: "0",
    overflowY: "scroll",
    color: "black", // 🔥 글자 색상을 흰색으로 변경
  } as const;

  const headerStyle = {
    padding: "16px",
    backgroundColor: "#3D67D7",
    color: "#fff",
    fontWeight: "bold",
    fontSize: "1.1rem",
    borderBottom: "1px solid #ccc",
    display: "flex",
    alignItems: "center",
    justifyContent: "space-between", // 🔥 버튼이 옆으로 정렬됨
  };

  const addButtonStyle = {
    backgroundColor: "#304a77", // 🔥 파란색 버튼
    color: "white",
    border: "none",
    borderRadius: "50%", // 🔥 동그랗게 변경
    width: "30px",
    height: "30px",
    fontSize: "20px",
    cursor: "pointer",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    fontWeight: "bold",
  };

  return (
      <>
        <div style={containerStyle}>
          <div style={headerStyle}>
            <h2 style={{margin: 0, fontSize: "18px"}}>채팅방 목록</h2>
            <button style={addButtonStyle} onClick={onCreateRoom}>+</button>
          </div>

          <div>
            {rooms.map((room) => (
                <div
                    key={room.roomId}
                    onClick={() => handleRoomClick(room.roomId,room.name)}
                    onContextMenu={(e) => {
                      e.preventDefault(); // 기본 우클릭 메뉴 막기
                      handleContextMenu(e, room);
                    }}
                    style={{
                      padding: "12px",
                      backgroundColor: room.roomId === selectedRoomId ? "white" : "#d6e0f0",
                      borderBottom: "1px solid #eee",
                      cursor: "pointer",
                    }}
                >
                  <div style={{fontWeight: "bold"}}>{room.roomName}</div>
                  <div style={{
                    fontSize: "12px",
                    color: "#666",
                    marginTop: "4px",
                    whiteSpace: "nowrap",
                    overflow: "hidden",
                    textOverflow: "ellipsis"
                  }}>
                    {room.lastMessage
                        ? (room.lastMessage.length > 30
                            ? room.lastMessage.slice(0, 30) + "..."
                            : room.lastMessage)
                        : "메시지가 없습니다"}
                  </div>
                  {(room.unreadCount ?? 0)> 0 && (
                      <span style={{
                        backgroundColor: "red",
                        color: "white",
                        fontSize: "10px",
                        borderRadius: "4px",
                        padding: "2px 6px",
                        marginLeft: "8px"
                      }}>
                             New
                        </span>
                  )}
                </div>
            ))}
          </div>
        </div>
        {contextMenu.visible && contextMenu.room && (
            <div
                style={{
                  position: "absolute",
                  top: contextMenu.y,
                  left: contextMenu.x,
                  backgroundColor: "white",
                  border: "1px solid #ccc",
                  zIndex: 1000,
                  padding: "8px",
                  boxShadow: "0 2px 8px rgba(0,0,0,0.15)",
                }}
            >
              <div
                  style={{ padding: "6px", cursor: "pointer" }}
                  onClick={() => {
                    const room = contextMenu.room!;
                    // 1. 방 정보 보기 처리
                    onViewRoomInfo(room);
                    setContextMenu({ ...contextMenu, visible: false });
                  }}
              >
                방 정보 보기
              </div>
              <div
                  style={{ padding: "6px", cursor: "pointer", color: "red" }}
                  onClick={() => {
                    handleLeaveRoom(); // 2. 방 나가기 처리
                    setContextMenu({ ...contextMenu, visible: false });
                  }}
              >
                방 나가기
              </div>
            </div>
        )}

      </>
  );

};

export default ChatRoomList;