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
    const [currentUser, setCurrentUser] = useState<string | null>(null);
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
                stompClient.subscribe(`/topic/chat/${currentRoomId}`, (message) => {
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
        const token = localStorage.getItem("accessToken");

        if (!token) {
            console.error("토큰이 없습니다. 로그인 페이지로 이동합니다.");
            router.push("/");
            return;
        }
        if (!currentUser) {
            console.log("⏳ currentUser 값이 설정되지 않음, employeeId 요청 지연...");
            return; // currentUser가 설정되기 전에는 요청 보내지 않음
        }
        fetch(`http://127.0.0.1:1006/employee/find?employeeId=${currentUser}`, {
            method: "GET",
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
            },
        })
            .then((res) => {
                if (!res.ok) {
                    throw new Error("❌ JWT 인증 실패: 응답 코드 " + res.status);
                }
                return res.json();
            })
            .then((data) => {
                console.log("✅ employeeId 가져오기 성공:", data);
            })
            .catch((err) => console.error("❌ 사용자 정보 가져오기 실패:", err));
    }, [currentUser]); // 🔥 currentUser가 변경될 때만 실행

    // //  메시지 전송 함수
    // const sendMessage = () => {
    //     if (stompClientRef.current && stompClientRef.current.connected) {
    //         stompClientRef.current.send(
    //             "/app/chat/send",
    //             {},
    //             JSON.stringify({ roomId: currentRoomId, sender: currentUser, message: input })
    //         );
    //         setInput("");
    //     }
    // };
    //

    return (
        <div style={{display: "flex", height: "100vh"}}>
            <ChatRoomList
                currentUser={currentUser}
                stompClient={stompClientRef.current}
                onSelectRoom={(roomId: string) => setCurrentRoomId(roomId)}
                onCreateRoom={() => setShowCreateModal(true)}
            />
            <ChatArea currentUser={currentUser} currentRoomId={currentRoomId} stompClient={stompClientRef.current}/>
            <RoomCreateModal
                visible={showCreateModal}
                onClose={() => setShowCreateModal(false)}
                onRoomCreated={() => console.log("방 생성 완료")}
                currentUser={currentUser}
            />
        </div>
    );
};


