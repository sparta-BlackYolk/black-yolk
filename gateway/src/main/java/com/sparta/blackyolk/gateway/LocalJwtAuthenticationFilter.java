package com.sparta.blackyolk.gateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;

// 로그 출력을 위한 Slf4j 사용
@Slf4j
// Spring Bean으로 등록 (GlobalFilter 구현체)
@Component
public class LocalJwtAuthenticationFilter implements GlobalFilter {

    // JWT Secret Key를 application.yml에서 주입받음
    @Value("${service.jwt.secret-key}")
    private String secretKey;

    // 필터 동작을 정의하는 메서드
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 요청 URI 경로를 가져옴
        String path = exchange.getRequest().getURI().getPath();
        // 특정 경로에 대해 필터를 건너뜀 (예: /auth/signIn, /auth/signUp)
        if (path.equals("/api/auth/users/signup") || path.equals("/api/auth/users/login")) {
            return chain.filter(exchange);  // /signIn 및 /signUp 경로는 필터 적용하지 않음
        }

        // Authorization 헤더에서 토큰 추출
        String token = extractToken(exchange);

        // 토큰이 없거나 유효하지 않을 경우 401 (Unauthorized) 응답
        if (token == null || !validateToken(token, exchange)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED); // 401 상태 코드 설정
            return exchange.getResponse().setComplete(); // 요청 종료
        }

        // 필터 체인을 통해 다음 필터로 요청을 전달
        return chain.filter(exchange);
    }

    // Authorization 헤더에서 Bearer 토큰을 추출하는 메서드
    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // "Bearer " 제거 후 토큰 반환
        }
        return null; // Authorization 헤더가 없거나 형식이 잘못된 경우 null 반환
    }

    // JWT 토큰을 검증하는 메서드
    private boolean validateToken(String token, ServerWebExchange exchange) {
        try {
            // Secret Key를 기반으로 HMAC-SHA 키 생성
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));

            // 토큰을 파싱하고 검증
            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(key) // Secret Key로 서명 검증
                    .build().parseSignedClaims(token); // 서명된 Claims를 파싱
            log.info("#####payload :: " + claimsJws.getPayload().toString()); // Payload 로그 출력

            // 파싱된 Claims에서 사용자 ID와 역할(Role) 추출
            Claims claims = claimsJws.getBody();
            exchange.getRequest().mutate()
                    .header("X-User-Id", claims.get("user_id").toString()) // 사용자 ID를 새로운 헤더에 추가
                    .header("X-Role", claims.get("role").toString()) // 역할(Role)을 새로운 헤더에 추가
                    .build();

            // 추가 검증 로직(예: 토큰 만료 여부 확인) 삽입 가능
            return true; // 검증 성공 시 true 반환
        } catch (Exception e) {
            // 검증 실패 시 false 반환
            return false;
        }
    }
}
