import React, { useState, useEffect } from "react";

interface RoomCreateModalProps {
    visible: boolean;
    onClose: () => void;
    onRoomCreated: () => void;
    currentUser: string | null;
}

const RoomCreateModal: React.FC<RoomCreateModalProps> = ({ visible, onClose, onRoomCreated, currentUser }) => {
    const [roomName, setRoomName] = useState("");
    const [members, setMembers] = useState<string[]>([]);
    const [allUsers, setAllUsers] = useState<string[]>([]);

    useEffect(() => {
        if (!visible) return;

        const token = localStorage.getItem("accessToken");

        fetch("http://127.0.0.1:1006/employee/getallemployeeids", {
            method: "GET",
            headers: {
                Authorization: `Bearer ${token}`,  // 🔥 인증 헤더 추가
                "Content-Type": "application/json"
            },
        })
            .then((res) => {
                if (!res.ok) {
                    throw new Error("❌ 인증 실패 또는 서버 오류");
                }
                return res.json();
            })
            .then((data: string[]) => setAllUsers(data.map((name: string) => name).filter((name) => name !== currentUser)))
            .catch((err) => console.error("회원 목록 불러오기 실패:", err));
    }, [visible]);

    function createRoom() {
        if (!roomName.trim()) {
            alert("방 이름을 입력하세요");
            return;
        }
        const selectedMembers = [...members, currentUser];
        fetch("http://127.0.0.1:1006/chat/rooms", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ roomName, name: selectedMembers }),
        })
            .then(() => {
                alert("방 생성 완료!");
                onRoomCreated();
                onClose();
            })
            .catch(() => alert("방 생성 실패"));
    }

    if (!visible) return null;

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <h3>방 생성</h3>
                <input
                    type="text"
                    placeholder="방 이름 입력"
                    value={roomName}
                    onChange={(e) => setRoomName(e.target.value)}
                />
                <div>
                    <h4>초대할 멤버</h4>
                    {allUsers.map((user) => (
                        <label key={user}>
                            <input
                                type="checkbox"
                                value={user}
                                onChange={(e) => {
                                    if (e.target.checked) setMembers([...members, user]);
                                    else setMembers(members.filter((m) => m !== user));
                                }}
                            />
                            {user}
                        </label>
                    ))}
                </div>
                <button onClick={createRoom}>생성</button>
                <button onClick={onClose}>취소</button>
            </div>
        </div>
    );
};

export default RoomCreateModal;
