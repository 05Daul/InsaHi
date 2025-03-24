import React, { useEffect, useState } from "react";
// import RoomCreateModal from "./RoomCreateModal";
// import RoomInfoModal from "./RoomInfoModal";
// import ContextMenu from "./ContextMenu";

interface ChatRoom {
    roomId: string;
    roomName: string;
}

interface ChatRoomListProps {
    currentUserId: string | null;
    stompClient: any;
    onSelectRoom: (roomId: string) => void;
    onCreateRoom: () => void;
}

const ChatRoomList: React.FC<ChatRoomListProps> = ({ currentUserId, stompClient, onSelectRoom, onCreateRoom  }) => {
    const [rooms, setRooms] = useState<ChatRoom[]>([]);
    const [showCreateModal, setShowCreateModal] = useState(false);
    const [roomInfo, setRoomInfo] = useState<ChatRoom | null>(null);
    const [contextMenu, setContextMenu] = useState<{ visible: boolean; x: number; y: number; room: ChatRoom | null }>({
        visible: false,
        x: 0,
        y: 0,
        room: null,
    });

    useEffect(() => {
        if (!currentUserId) return;
        fetchRooms();
    }, [currentUserId]);

    function fetchRooms() {
        const token = localStorage.getItem("accessToken");
        if (!token) {
            console.error("❌ 토큰 없음");
            return;
        }

        fetch(`http://127.0.0.1:1006/chat/rooms/member/${currentUserId}`, {
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

    function handleRoomClick(roomId: string) {
        onSelectRoom(roomId);
    }

    function handleContextMenu(e: React.MouseEvent, room: ChatRoom) {
        e.preventDefault();
        setContextMenu({ visible: true, x: e.pageX, y: e.pageY, room });
    }

    function handleLeaveRoom() {
        if (!contextMenu.room) return;
        fetch(`http://127.0.0.1:1006/chat/rooms/${contextMenu.room.roomId}/members/${currentUserId}`, {
            method: "DELETE",
        })
            .then(() => {
                alert("방에서 나갔습니다.");
                fetchRooms();
            })
            .catch(() => alert("방 나가기 실패"));

        setContextMenu({ visible: false, x: 0, y: 0, room: null });
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
        color: "white", // 🔥 글자 색상을 흰색으로 변경
    } as const;

    const headerStyle = {
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between", // 🔥 버튼이 옆으로 정렬됨
        backgroundColor: "#5c7fbc", // 🔥 진한 파란색으로 변경
        padding: "10px",
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
        <div style={containerStyle}>
            <div style={headerStyle}>
                <h2 style={{ margin: 0, fontSize: "18px" }}>채팅방 목록</h2>
                <button style={addButtonStyle} onClick={onCreateRoom}>+</button>
            </div>

            <div>
                {rooms.map((room) => (
                    <div
                        key={room.roomId}
                        style={{ padding: 10, borderBottom: "1px solid white", cursor: "pointer" }}
                        onDoubleClick={() => handleRoomClick(room.roomId)}
                        onContextMenu={(e) => handleContextMenu(e, room)}
                    >
                        {room.roomName}
                    </div>
                ))}
            </div>
        </div>

            // // {/*<RoomCreateModal*/}
            // {/*    visible={showCreateModal}*/}
            // {/*    onClose={() => setShowCreateModal(false)}*/}
            // {/*    onRoomCreated={fetchRooms}*/}
            // {/*    currentUser={currentUser}*/}
            // {/*/>*/}
            // {/*<RoomInfoModal visible={!!roomInfo} onClose={() => setRoomInfo(null)} room={roomInfo} />*/}
            //
            // {/*<ContextMenu*/}
            // {/*    x={contextMenu.x}*/}
            // {/*    y={contextMenu.y}*/}
            // {/*    visible={contextMenu.visible}*/}
            // {/*    onClose={() => setContextMenu({ visible: false, x: 0, y: 0, room: null })}*/}
            // {/*    menuItems={[{ label: "나가기", onClick: handleLeaveRoom }]}*/}
            // // {/*/>*/}
    );
};

export default ChatRoomList;
