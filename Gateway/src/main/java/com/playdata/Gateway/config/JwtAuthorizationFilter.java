package com.playdata.Gateway.config;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class JwtAuthorizationFilter extends AbstractGatewayFilterFactory<JwtAuthorizationFilter.Config> {
    //yml파일에 정의된 속성 사용하기 위해서
    private Environment environment;
    public JwtAuthorizationFilter(Environment environment) {
        super(Config.class);
        this.environment = environment;
    }
    public static class Config {

    }
    //1. 인증 후에 실행할 기능은 필터를 통해서 요청되도록 처리
    //2. 요청헤더에 토큰이 있는지 확인
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            //request할때 실행할 내용을 정의 - request헤더에 토큰이 있는지 검증
            System.out.println("===================pre필터(request할때 실행되는 필터)==============");
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            //AUTHORIZATION헤더가 있으면 다음으로 진행이 될 수 있도록 작업 - chain.filter호출
            //만약에 요청헤더에 AUTHORIZATION이라는 헤더가 없는 경우 오류상황
            //HttpStatus.UNAUTHORIZED를 넘기면서 현재 인증되지 못했다는 상태코드를 셋팅함
//            String token = getToken((HttpServletRequest) request);
//            System.out.println("request ====>>>>"+token);
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "헤더에 AUTHORIZATION이 없습니다.", HttpStatus.UNAUTHORIZED);
            }

            //토큰이 있는 경우 토큰을 꺼내서 유효한지 검증 - 토큰만들때 사용한 시크릿키와 파싱할때 사용한 시크릿키가 같은지
            String token = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            System.out.println("토큰:"+token);
            token = token.substring(7).trim();
            if(!isTokenValid(token)) {
                return onError(exchange,"토큰이 유효하지않습니다.",HttpStatus.UNAUTHORIZED);
            }
            System.out.println("bear 제거토큰 : "+token);
            return chain.filter(exchange).then(Mono.fromRunnable(new Runnable() {
                @Override
                public void run() {
                    System.out.println("===============post필터(response할때 실행되는 필터)==============");
                    //response할때 실행할 내용을 필터로 정의
                }
            }));

        };
    }
//    //클라이언트의 요청정보에서 토큰을 꺼내서 리턴하는 메소드
//    public String getToken(HttpServletRequest request) {
//        //Authorization헤더에서 토큰꺼내기
//        String bearerToken = request.getHeader("Authorization");
//        System.out.println("🔍 요청된 Authorization 헤더: " + bearerToken);
//        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//            String token =bearerToken.substring(7);
//            System.out.println("📌 추출된 JWT 토큰: " + token);
//            return token; //"Bearer " 제거 후 JWT 리턴
//        }
//        return null; // 토큰이 없거나 Bearer 형식이 아닌 경우 null 리턴
//    }

    //토큰에 대한 검증
    private boolean isTokenValid(String token) {


        boolean isValid = true;
        String parsingToken = "";
//        token = token.replace("Bearer ", "");

        System.out.println(token);
        try {
            //토큰을 파싱해서 값을 꺼내고 비교
            //시크릿키가 토큰만들어질때의 값과 다르면 파싱하면서 오류가 발생
            parsingToken = Jwts.parser().setSigningKey(environment.getProperty("jwt.secret"))
                    .parseClaimsJws(token).getBody().getSubject();
            System.out.println(parsingToken);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        if (parsingToken == null || parsingToken.isEmpty()) {
            return false;
        }
        return isValid;
    }
    //에러 상황이면 바로 다음으로 넘어가지 못하고 response되도록 처리
    private Mono<Void> onError(ServerWebExchange exchange, String msg, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        log.error(msg);
        return response.setComplete();//응답완료
    }


}