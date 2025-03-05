//package com.playdata.User.employee.authentication;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.io.Decoder;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//import java.util.stream.Collectors;
//
////JWT(Json Web Token)를 생성, 검증, 파싱하는 역할을 담당하는 토큰 관리 클래스
////JWT 기반의 인증(Authentication)을 구현할 때 사용
//@Component
//public class TokenManager implements InitializingBean {
//    private String secret;
//    private long tokenExpiryTime;
//    private Key key;    //JWT 서명 및 검증을 위한 키
//
//    public TokenManager(@Value("${jwt.secret}") String secret,
//                        @Value("${jwt.token-validity-in-second}") long tokenExpiryTime) {
//        this.secret = secret;
//        this.tokenExpiryTime = tokenExpiryTime;
//    }
//
//
//    @Override
//    public void afterPropertiesSet() throws Exception { //secret 키 값을 Base64 디코딩한 후, HMAC-SHA 암호화 키로 변환하여 저장
//        //주입이 되면 자동으로 호출되는 메소드
//        //시크릿키를 디코딩을 해서 키를 생성
//        byte[] keydata = Decoders.BASE64.decode(secret);
//        this.key= Keys.hmacShaKeyFor(keydata);
//    }
//
//    //인증에 성공하면 토큰 생성
//    public String createToken(Authentication authentication){
//        String authoritylist = authentication.getAuthorities().stream()
//                //각 GrantedAuthority 객체에서 문자열 권한 값을 추출
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));
//
//        long nowtime = new Date().getTime();
//        Date targetTime = new Date(nowtime+tokenExpiryTime); //현재 시간과 tokenExpiryTime을 더해서 만료 시간 저장
//
//        return Jwts.builder()
//                .setSubject(authentication.getName()) //사용자 아이디 저장
//                .claim("auth", authoritylist) //권한 정보를 JWT에 포함
//                .signWith(key, SignatureAlgorithm.HS256) //HS256 알고리즘을 사용하여 서명
//                .setExpiration(targetTime) //만료 시간 설정
//                .compact(); //최종 JWT 토큰 생성
//    }
//
//    public Authentication getAuthentication(String token){
//        Claims claims = Jwts
//                .parserBuilder()
//                .setSigningKey(key)
//                .build() //토큰을 파싱하여 Claims 객체 획득
//                .parseClaimsJwt(token)
//                .getBody(); //토큰에서 사용자 정보, 권한 정보를 추출
//
//        List<GrantedAuthority> authoritylist = Arrays.stream(claims.get("auth").toString().split(","))
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList()); //JWT에서 auth 값을 가져와 SimpleGrantedAuthority 리스트로 변환
//
//        //사용자 아이디(subject)와 권한 리스트를 포함하는 객체 생성
//        User principal = new User(claims.getSubject(), "", authoritylist);
//        //UsernamePasswordAuthenticationToken을 사용하여 인증 객체 생성
//        return new UsernamePasswordAuthenticationToken(principal, token, authoritylist);
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            Jws<Claims> claimsJws = Jwts.parserBuilder()
//                    .setSigningKey(key)
//                    .build()
//                    .parseClaimsJws(token); //parseClaimsJws(token)을 실행하여 파싱이 성공하면 true 반환
//            return true;
//        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
//            System.out.println("잘못된 형식으로 서명된 토큰");
//        } catch (ExpiredJwtException e) {
//            System.out.println("만료된 토큰");
//        } catch (UnsupportedJwtException e) {
//            System.out.println("지원되지 않는 토큰");
//        } catch (IllegalArgumentException e) {
//            System.out.println("잘못된 토큰");
//        }
//        return false;
//    }
//
//}
