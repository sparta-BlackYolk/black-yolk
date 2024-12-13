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
        log.info("Incoming request path: {}", path); // 요청 경로 로그 출력

        // 특정 경로에 대해 필터를 건너뜀 (예: /auth/signup, /auth/login)
        if (path.equals("/api/auth/users/signup") || path.equals("/api/auth/users/login")) {
            log.info("Path {} is excluded from authentication", path); // 인증 제외 경로 로그 출력
            return chain.filter(exchange);
        }

        // Authorization 헤더에서 토큰 추출
        String token = extractToken(exchange);
        log.info("Extracted token: {}", token); // 추출된 토큰 로그 출력

        // 토큰이 없거나 유효하지 않을 경우 401 (Unauthorized) 응답
        if (token == null) {
            log.warn("Token is missing in the request"); // 토큰이 없는 경우 경고 로그 출력
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // JWT 토큰 검증
        if (!validateToken(token, exchange)) {
            log.warn("Token validation failed for token: {}", token); // 검증 실패 시 경고 로그 출력
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        log.info("Token validation succeeded"); // 검증 성공 로그 출력
        return chain.filter(exchange); // 필터 체인을 통해 요청 전달
    }

    // Authorization 헤더에서 Bearer 토큰을 추출하는 메서드
    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        log.info("Authorization header: {}", authHeader); // Authorization 헤더 로그 출력
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
            log.info("Decoded secret key: {}", key);

            // 토큰을 파싱하고 검증
            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(key) // Secret Key로 서명 검증
                    .build().parseSignedClaims(token);
            log.info("JWT payload: {}", claimsJws.getPayload());

            // 파싱된 Claims에서 사용자 정보 추출
            Claims claims = claimsJws.getBody();
            log.info("Extracted Claims: {}", claims);

            // 헤더에 사용자 정보 추가
            exchange.getRequest().mutate()
                    .header("X-User-Id", claims.getSubject()) // 'sub' 필드 사용
                    .header("X-Role", claims.get("role").toString()) // 역할(Role)을 새로운 헤더에 추가
//                    .header("X-Role", claims.get("auth").toString()) // 'auth' 필드 사용
                    .build();

            return true; // 검증 성공
        } catch (Exception e) {
            log.error("JWT validation error: {}", e.getMessage(), e);
            return false; // 검증 실패
        }
    }

}
