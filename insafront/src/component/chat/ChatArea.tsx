import React, { useEffect, useState, useRef } from "react";
// import ContextMenu from "./ContextMenu";

interface Message {
    chatId: string;
    name: string;
    content: string;
    deleted?: boolean;
}

interface ChatAreaProps {
    currentUserName: string | null;
    currentRoomId: string | null;
    stompClient: any | null;
}

const ChatArea: React.FC<ChatAreaProps> = ({ currentUserName, currentRoomId, stompClient }) => {
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
            .then((data) => {console.log("📥 메시지 로드 성공:", data);
                setMessages(data);
            })
            .catch((err) => console.error("메시지 불러오기 실패:", err));
    }, [currentRoomId]);

    // ✅ WebSocket 메시지 구독
    useEffect(() => {
        if (!stompClient || !currentRoomId) return;

        const subscription = stompClient.subscribe(`/topic/messages/${currentRoomId}`, (message: any) => {
            const newMessage = JSON.parse(message.body);
            console.log("📨 실시간 메시지 도착:", newMessage);
            setMessages((prevMessages) => [...prevMessages, newMessage]);
        });

        return () => subscription.unsubscribe();
    }, [stompClient, currentRoomId]);

    // ✅ 메시지 보내기
    async function sendMessage() {
        if (!stompClient || !currentRoomId || !messageRef.current) return;
        const message = messageRef.current.value.trim();
        if (!message) return;

        const formData = new FormData();
        formData.append("name", currentUserName!);
        formData.append("roomId", currentRoomId);
        formData.append("content", message);

        try {
            const res = await fetch("http://127.0.0.1:1006/chat/send", {
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

    // ✅ 우클릭 메뉴 열기
    function handleContextMenu(e: React.MouseEvent, msg: Message) {
        e.preventDefault();
        if (msg.name !== currentUserName) {
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
            <div style={{
                flex: 1,
                display: "flex",
                flexDirection: "column",
                gap: "10px",
                marginLeft: "25%",
                marginTop: "60px",  // Topbar 영역 확보
                marginBottom: "70px", // 하단 입력창 고정 영역 확보
                padding: "10px"

            }}>
                {messages.map((msg) => (
                    <div
                        key={msg.chatId}
                        onContextMenu={(e) => handleContextMenu(e, msg)}
                        style={{
                            display: "block",                            padding: "10px",
                            backgroundColor: msg.name === currentUserName ? "#e1ffc7" : "#f1f1f1",
                            marginBottom: "10px",
                            borderRadius: "10px",
                            maxWidth: "70%",
                            minWidth: "120px",
                            alignSelf: msg.name === currentUserName ? "flex-end" : "flex-start",
                            wordBreak: "break-word",
                            color: "#000", // 혹시라도 글자색과 배경색이 겹칠까봐 지정
                        }}
                    >
                        <strong>{msg.name}</strong>: {msg.deleted ? <i>삭제된 메시지입니다.</i> : msg.content}
                    </div>
                ))}

            {/* 메시지 입력창 */}
                <div
                    style={{
                        position: "fixed",
                        bottom: 0,
                        left: "25%", // ChatRoomList의 너비만큼 띄움
                        width: "75%",
                        backgroundColor: "#fff",
                        padding: "10px",
                        display: "flex",
                        borderTop: "1px solid #ccc",
                        boxShadow: "0 -2px 5px rgba(0,0,0,0.1)",
                    }}
                >
                    <input type="text" ref={messageRef} placeholder="메시지를 입력하세요" onKeyDown={(e) => {
                        if (e.key === "Enter") sendMessage();
                    }} style={{flex: 1, padding: "5px"}}/>
                    <button onClick={sendMessage} style={{marginLeft: "5px"}}>전송</button>
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
