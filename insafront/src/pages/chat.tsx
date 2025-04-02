import React, {useEffect, useRef, useState} from "react";
import ChatRoomList from "@/component/chat/ChatRoomList";
import ChatArea from "@/component/chat/ChatArea";
import RoomCreateModal from "@/component/chat/RoomCreateModal";
import SockJS from "sockjs-client";
import Stomp, {Client} from "stompjs";
import {useRouter} from "next/router";
import RoomInfoModal from "@/component/chat/RoomInfoModal";


const SOCKET_URL = "http://127.0.0.1:1006/chat/ws-stomp"; //  Spring Boot와 일치

interface Message {
  text: string;
}

export default function Chat() {
  const [messages, setMessages] = useState<Message[]>([]);
  const stompClientRef = useRef<Client | null>(null);
  const [currentUserId, setCurrentUserId] = useState("");
  const [currentUserName, setCurrentUserName] = useState("");
  const [currentRoomId, setCurrentRoomId] = useState<string | null>(null);
  const [showCreateModal, setShowCreateModal] = useState(false);
  const router = useRouter();
  const [reloadRooms, setReloadRooms] = useState(false);
  const [participantCount, setParticipantCount] = useState(0);
  // 내가 방 들어갈 때 or 새 메시지 받을 때 방 목록을 다시 불러오기 위해
  const handleReloadRooms = () => setReloadRooms((prev) => !prev);
  const handleSelectRoom = (roomId: string, name: string[]) => {
    setSelectedRoomId(roomId);
    setParticipantCount(name.length);
  };

  const [roomInfoModalVisible, setRoomInfoModalVisible] = useState(false);
  const [selectedRoomId, setSelectedRoomId] = useState<string | null>(null);
  const [selectedRoomInfo, setSelectedRoomInfo] = useState<{
    roomId: string;
    roomName: string;
    name: string[];
    createdAt: string;
    creatorName: string;
  } | null>(null);

  useEffect(() => {
    if (typeof window === "undefined") return;
    if (stompClientRef.current) return; // 중복 연결 방지

    const socket = new SockJS(SOCKET_URL); //  SockJS 사용
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
      console.log("WebSocket 연결 성공!");

      // 현재 방이 있으면 해당 방 구독
      if (currentRoomId) {
        stompClient.subscribe(`/topic/messages/${currentRoomId}`, (message) => {
          setMessages((prev) => [...prev, JSON.parse(message.body)]);
        });
      }
    });

    stompClientRef.current = stompClient;

    return () => {
      if (stompClientRef.current) {
        stompClientRef.current.disconnect(() => {
          console.log("WebSocket 연결 해제됨.");
        });
        stompClientRef.current = null;
      }
    };
  }, [currentRoomId]); // 방이 변경될 때마다 재구독


  useEffect(() => {
    const fetchUser = async () => {
      const token = localStorage.getItem("accessToken");

      if (!token) {
        console.error("❌ accessToken 없음 → 로그인 페이지로 이동");
        router.push("/");
        return;
      }

      try {
        const payload = JSON.parse(atob(token.split(".")[1]));
        const employeeId = payload.sub?.trim();
        if (employeeId) {
          setCurrentUserId(employeeId);
          const res = await fetch(`http://127.0.0.1:1006/employee/find?employeeId=${employeeId}`, {
            method: "GET",
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "application/json",
            },
          });

          if (!res.ok) {
            const msg = await res.text();
            throw new Error("❌ JWT 인증 실패: " + msg);
          }

          const data = await res.json();
          setCurrentUserName(data.name);
          console.log("✅ employeeId 가져오기 성공:", data);
        } else {
          console.error("❌ employeeId 없음");
        }
      } catch (err) {
        console.error("❌ 사용자 정보 가져오기 실패:", err);
      }
    };

    fetchUser();
  }, []);


  return (
      <div style={{display: "flex", flexDirection: "row", marginTop: "50px"}}>
        <ChatRoomList
            currentUserName={currentUserName}
            stompClient={stompClientRef.current}
            onSelectRoom={(roomId: string) => {
              setCurrentRoomId(roomId);
              setSelectedRoomId(roomId);
            }}
            reloadRooms={reloadRooms}
            onCreateRoom={() => setShowCreateModal(true)}
            onViewRoomInfo={(room) => {
              setSelectedRoomInfo(room);
              setRoomInfoModalVisible(true);
            }}
            selectedRoomId={selectedRoomId}
        />

        <RoomInfoModal
            visible={roomInfoModalVisible}
            onClose={() => setRoomInfoModalVisible(false)}
            room={selectedRoomInfo}
        />

        <ChatArea currentUserName={currentUserName} currentRoomId={currentRoomId}
                  stompClient={stompClientRef.current}
                  participantCount={selectedRoomInfo?.name?.length || 1}
                  onNewMessageArrived={handleReloadRooms} // 새 메시지 도착 => reloadRooms
                  onMessagesRead={handleReloadRooms}      // 메시지 읽음 => reloadRooms
        />
        <RoomCreateModal
            visible={showCreateModal}
            onClose={() => setShowCreateModal(false)}
            onRoomCreated={() => setReloadRooms(prev => !prev)}
            currentUserName={currentUserName}
            currentUserId={currentUserId}
        />
      </div>
  );
};