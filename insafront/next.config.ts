import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  reactStrictMode: true,
  async rewrites() {
    return [
      {
        source: '/api/:path*',
        destination: 'http://localhost:1010/api/:path*', // 백엔드 서버로 리다이렉트
      },
    ];
  },
  /* config options here */
};

export default nextConfig;
