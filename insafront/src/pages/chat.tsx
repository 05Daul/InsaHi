import React, { useEffect, useState, useRef } from "react";
import ChatRoomList from "@/component/chat/ChatRoomList";
import ChatArea from "@/component/chat/ChatArea";
import RoomCreateModal from "@/component/chat/RoomCreateModal";
import SockJS from "sockjs-client";
import Stomp, {Client} from "stompjs";
import {useRouter} from "next/router";


const SOCKET_URL = "http://127.0.0.1:1006/chat/ws-stomp"; //  Spring Boot와 일치

interface Message {
    text: string;
}

export default function Chat() {
    const [messages, setMessages] = useState<Message[]>([]);
    const [input, setInput] = useState("");
    const stompClientRef = useRef<Client | null>(null);
    const [currentUserId, setCurrentUserId] = useState("");
    const [currentUserName, setCurrentUserName] = useState("");
    const [currentRoomId, setCurrentRoomId] = useState<string | null>(null);
    const [showCreateModal, setShowCreateModal] = useState(false);
    const router = useRouter();

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
                    const res = await fetch(`http://localhost:1006/employee/find?employeeId=${employeeId}`, {
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
                currentUserId={currentUserId}
                stompClient={stompClientRef.current}
                onSelectRoom={(roomId: string) => setCurrentRoomId(roomId)}
                onCreateRoom={() => setShowCreateModal(true)}
            />
            <ChatArea currentUserName={currentUserName} currentRoomId={currentRoomId} stompClient={stompClientRef.current}/>
            <RoomCreateModal
                visible={showCreateModal}
                onClose={() => setShowCreateModal(false)}
                onRoomCreated={() => console.log("방 생성 완료")}
                currentUserName={currentUserName}
            />
        </div>
    );
};


