import "@/styles/globals.css";
import type { AppProps } from "next/app";
import MainLayout from "@/component/MainLayout";
// import SockJS from 'sockjs-client';
// import Stomp from 'stompjs';
import {useEffect, useState} from "react";
import {useRouter} from "next/router";
import RoomList from "@/component/chat/ChatRoomList";
import ChatArea from "@/component/chat/ChatArea";

export default function App({ Component, pageProps }: AppProps) {

    const router = useRouter();

    // 로그인과 회원가입 페이지에서는 MainLayout을 사용하지 않음
    const noLayoutPaths = ["/Login", "/SignupForm","/"];
    const isNoLayoutPage = noLayoutPaths.includes(router.pathname);
//  WebSocket 연결 상태 관리
//     const [stompClient, setStompClient] = useState<any>(null);
//     const [currentUser, setCurrentUser] = useState<string | null>(null);
//     const [currentRoomId, setCurrentRoomId] = useState<string | null>(null);

    useEffect(() => {
        const publicPaths = ["/Login", "/SignupForm","/"]; // 인증 없이 접근 가능한 페이지
        const pathIsPublic = publicPaths.includes(router.pathname);

        const token = localStorage.getItem("accessToken");

        if (!token && !pathIsPublic) {
            // window.location.href ="/"
            router.push("/"); // 로그인 안 했으면 로그인 페이지로 이동
        }
    }, [router.pathname]);
    // useEffect(() => {
    //     // ✅ 로그인된 사용자 정보 가져오기
    //     fetch("http://127.0.0.1:9500/erp/member/getSessionUser", { credentials: "include" })
    //         .then((res) => res.json())
    //         .then((data) => {
    //             if (data.status === "success") {
    //                 setCurrentUser(data.name);
    //                 connectWebSocket(data.name);
    //             } else {
    //                 console.error("로그인 정보 없음");
    //             }
    //         })
    //         .catch((err) => console.error("로그인 정보 가져오기 실패:", err));
    // }, []);
    //
    // function connectWebSocket(userName: string) {
    //     if (!userName) return;

    // const socket = new SockJS("http://127.0.0.1:9500/chat/ws-stomp");
    // const stomp = Stomp.over(socket);

    // stomp.connect({}, (frame) => {
    //     console.log("✅ WebSocket 연결 완료:", frame);
    // }, (error) => {
    //     console.error("❌ WebSocket 연결 실패:", error);
    // });
    //
    // setStompClient(stomp);
    // }


  return isNoLayoutPage ? (
      <Component {...pageProps} />
  ) : (
      <MainLayout>
          <Component {...pageProps} />
      </MainLayout>
  );
}