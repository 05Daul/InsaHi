import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  reactStrictMode: false,
  async rewrites() {
    return [
      {
        source: '/api/:path*',
        destination: 'http://localhost:1010/api/:path*', // 백엔드 서버로 리다이렉트
      },
    ];
  },
  /* config options here */




    webpack: (nextConfig: NextConfig,{isServer}) => {
      if(! isServer) {
        nextConfig.resolve.fallback = {
          net: false, //  'net' 모듈을 무시하여 오류 방지
          tls: false, // 'tls' 모듈도 함께 제외 (권장)
          fs: false, //  파일 시스템 관련 모듈도 제외 (필요한 경우)
        };
      }
      return nextConfig;
    },
};
module.exports = nextConfig;
