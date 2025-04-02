import {useEffect, useRef} from "react";

const RoomInfoModal = ({
                         visible,
                         onClose,
                         room,
                       }: {
  visible: boolean;
  onClose: () => void;
  room: {
    roomId: string;
    roomName: string;
    name: string[];
    createdAt: string;
    creatorName: string;
  } | null;
}) => {
  const modalRef = useRef<HTMLDivElement>(null);

  // ✅ 모달 외부 클릭 시 닫기
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (
          modalRef.current &&
          !modalRef.current.contains(event.target as Node)
      ) {
        onClose();
      }
    };

    if (visible) {
      document.addEventListener("mousedown", handleClickOutside);
    }

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [visible, onClose]);
  if (!visible || !room) return null; // ✅ room이 없으면 렌더링 안 함

    return (
        <div
            style={{
                position: "fixed",
                top: 0,
                left: 0,
                width: "100vw",
                height: "100vh",
                backgroundColor: "rgba(0, 0, 0, 0.4)",
                zIndex: 1000,
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
            }}
        >
            <div
                ref={modalRef}
                style={{
                    backgroundColor: "white",
                    padding: "30px",
                    borderRadius: "16px",
                    boxShadow: "0 6px 20px rgba(0, 0, 0, 0.2)",
                    width: "400px",
                    maxWidth: "90%",
                }}
            >
                <h2 style={{ marginTop: 0, marginBottom: "10px", color: "#1E3A8A" }}>
                    방 정보
                </h2>
                <div style={{ marginBottom: "10px" }}>
                    <strong>방 이름:</strong> {room.roomName}
                </div>
                <div style={{ marginBottom: "10px" }}>
                    <strong>참여자 수:</strong> {room.name?.length ?? 0}명
                </div>
                <div style={{ marginBottom: "10px" }}>
                    <strong>참여자:</strong>{" "}
                    {room.name && room.name.length > 0
                        ? room.name.join(", ")
                        : "참여자 없음"}
                </div>
                <div style={{ marginBottom: "10px" }}>
                    <strong>방 생성자:</strong> {room.creatorName}
                </div><div style={{ marginBottom: "10px" }}>
                    <strong>방 개설일:</strong> {new Date(room.createdAt).toLocaleString()}
                </div>
                <div style={{ textAlign: "right", marginTop: "20px" }}>
                    <button
                        onClick={onClose}
                        style={{
                            padding: "8px 16px",
                            backgroundColor: "#3D67D7",
                            color: "#fff",
                            border: "none",
                            borderRadius: "6px",
                            cursor: "pointer",
                        }}
                    >
                        닫기
                    </button>
          </div>
        </div>
        </div>
  );

};

export default RoomInfoModal;