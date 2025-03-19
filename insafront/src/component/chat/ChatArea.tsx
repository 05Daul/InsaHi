import React, { useEffect, useState, useRef } from "react";
// import ContextMenu from "./ContextMenu";

interface Message {
    chatId: string;
    name: string;
    text: string;
    deleted?: boolean;
}

interface ChatAreaProps {
    currentUser: string | null;
    currentRoomId: string | null;
    stompClient: any | null;
}

const ChatArea: React.FC<ChatAreaProps> = ({ currentUser, currentRoomId, stompClient }) => {
    const [messages, setMessages] = useState<Message[]>([]);
    const [contextMenu, setContextMenu] = useState<{ visible: boolean; x: number; y: number; msg: Message | null }>({
        visible: false,
        x: 0,
        y: 0,
        msg: null,
    });
    const messageRef = useRef<HTMLInputElement>(null);

    // ✅ 채팅방 변경 시 메시지 불러오기
    useEffect(() => {
        if (!currentRoomId) return;

        fetch(`http://127.0.0.1:1006/chat/messages/${currentRoomId}`)
            .then((res) => res.json())
            .then((data) => setMessages(data))
            .catch((err) => console.error("메시지 불러오기 실패:", err));
    }, [currentRoomId]);

    // ✅ WebSocket 메시지 구독
    useEffect(() => {
        if (!stompClient || !currentRoomId) return;

        const subscription = stompClient.subscribe(`/topic/chat/${currentRoomId}`, (message: any) => {
            const newMessage = JSON.parse(message.body);
            setMessages((prevMessages) => [...prevMessages, newMessage]);
        });

        return () => subscription.unsubscribe();
    }, [stompClient, currentRoomId]);

    // ✅ 메시지 보내기
    function sendMessage() {
        if (!stompClient || !currentRoomId || !messageRef.current) return;
        const message = messageRef.current.value.trim();
        if (!message) return;

        const chatMessage = { name: currentUser, text: message, roomId: currentRoomId };
        stompClient.send(`/app/chat/${currentRoomId}`, {}, JSON.stringify(chatMessage));

        messageRef.current.value = "";
    }

    // ✅ 우클릭 메뉴 열기
    function handleContextMenu(e: React.MouseEvent, msg: Message) {
        e.preventDefault();
        if (msg.name !== currentUser) {
            alert("본인이 보낸 메시지만 삭제 가능합니다.");
            return;
        }
        setContextMenu({ visible: true, x: e.pageX, y: e.pageY, msg });
    }

    // ✅ 메시지 삭제
    function handleDeleteMessage() {
        if (!contextMenu.msg) return;
        const chatId = contextMenu.msg.chatId;

        fetch(`http://127.0.0.1:1006/chat/messages/${chatId}`, { method: "DELETE" })
            .then((res) => {
                if (!res.ok) throw new Error("삭제 실패");
                setMessages((prev) => prev.map((m) => (m.chatId === chatId ? { ...m, deleted: true } : m)));
            })
            .catch((err) => console.error(err))
            .finally(() => setContextMenu({ visible: false, x: 0, y: 0, msg: null }));
    }

    return (
        <div style={{ flex: 1, display: "flex", flexDirection: "column", padding: "10px" }}>
            {/* 메시지 목록 */}
            <div style={{ flex: 1, overflowY: "auto", borderBottom: "1px solid #ccc" }}>
                {messages.map((msg) => (
                    <div
                        key={msg.chatId}
                        onContextMenu={(e) => handleContextMenu(e, msg)}
                        style={{
                            padding: "10px",
                            backgroundColor: msg.name === currentUser ? "#e1ffc7" : "#f1f1f1",
                            marginBottom: "5px",
                            borderRadius: "10px",
                            alignSelf: msg.name === currentUser ? "flex-end" : "flex-start",
                            maxWidth: "70%",
                        }}
                    >
                        <strong>{msg.name}</strong>: {msg.deleted ? <i>삭제된 메시지입니다.</i> : msg.text}
                    </div>
                ))}
            </div>

            {/* 메시지 입력창 */}
            <div style={{ display: "flex", padding: "10px" }}>
                <input type="text" ref={messageRef} placeholder="메시지를 입력하세요" style={{ flex: 1, padding: "5px" }} />
                <button onClick={sendMessage} style={{ marginLeft: "5px" }}>전송</button>
            </div>

            {/*/!* Context Menu (우클릭 메뉴) *!/*/}
            {/*<ContextMenu*/}
            {/*    x={contextMenu.x}*/}
            {/*    y={contextMenu.y}*/}
            {/*    visible={contextMenu.visible}*/}
            {/*    onClose={() => setContextMenu({ visible: false, x: 0, y: 0, msg: null })}*/}
            {/*    menuItems={[{ label: "삭제", onClick: handleDeleteMessage }]}*/}
            {/*/>*/}
        </div>
    );
};

export default ChatArea;
