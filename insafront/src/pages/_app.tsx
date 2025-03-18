import "@/styles/globals.css";
import type { AppProps } from "next/app";
import MainLayout from "@/component/MainLayout";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import React, { useState } from "react";

export default function App({ Component, pageProps }: AppProps) {
    const [queryClient] = useState(() => new QueryClient());

    return (
        <QueryClientProvider client={queryClient}>
            <MainLayout>
                <Component {...pageProps} />
            </MainLayout>
        </QueryClientProvider>
    );
}
