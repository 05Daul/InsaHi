import React from "react";

interface RoomInfoModalProps {
    visible: boolean;
    onClose: () => void;
    room: { roomId: string; roomName: string; members: string[]; createdAt: string; creatorName: string } | null;
}

const RoomInfoModal: React.FC<RoomInfoModalProps> = ({ visible, onClose, room }) => {
    if (!visible || !room) return null;

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <h3>방 정보</h3>
                <p><strong>방 이름:</strong> {room.roomName}</p>
                <p><strong>방 개설자:</strong> {room.creatorName}</p>
                <p><strong>개설일:</strong> {room.createdAt}</p>
                <p><strong>참여자:</strong></p>
                <ul>
                    {room.members.map((member) => (
                        <li key={member}>{member}</li>
                    ))}
                </ul>
                <button onClick={onClose}>닫기</button>
            </div>
        </div>
    );
};

export default RoomInfoModal;
