import "@/styles/globals.css";
import type {AppProps} from "next/app";
import MainLayout from "@/component/MainLayout";
import {useEffect} from "react";
import {useRouter} from "next/router";
import {QueryClient, QueryClientProvider} from "@tanstack/react-query";

// QueryClient 생성
const queryClient = new QueryClient();

export default function App({Component, pageProps}: AppProps) {
  const router = useRouter();

  // 로그인과 회원가입 페이지에서는 MainLayout을 사용하지 않음
  const noLayoutPaths = ["/Login", "/SignupForm", "/"];
  const isNoLayoutPage = noLayoutPaths.includes(router.pathname);

  useEffect(() => {
    const publicPaths = ["/Login", "/SignupForm", "/"]; // 인증 없이 접근 가능한 페이지
    const pathIsPublic = publicPaths.includes(router.pathname);
    const token = localStorage.getItem("accessToken");

    if (!token && !pathIsPublic) {
      router.push("/"); // 로그인 안 했으면 로그인 페이지로 이동
    }
  }, [router.pathname]);

  return (
      // QueryClientProvider로 전체 애플리케이션 감싸기
      <QueryClientProvider client={queryClient}>
        {isNoLayoutPage ? (
            <Component {...pageProps} />
        ) : (
            <MainLayout>
              <Component {...pageProps} />
            </MainLayout>
        )}
      </QueryClientProvider>
  );
}