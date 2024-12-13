package com.sparta.blackyolk.auth_service.jwt;

import com.sparta.blackyolk.auth_service.user.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    @Value("${JWT_SECRET_KEY}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
        logger.info("Secret key initialized for JWT");

        // 서버 시간 확인 로그 추가
        logger.info("서버 현재 시간: {}", new Date());
    }


    public String createToken(String username, UserRoleEnum role) {
        Date date = new Date();
        Date expiration = new Date(date.getTime() + TOKEN_TIME);


        String token =  Jwts.builder()
                .setSubject(username) // 사용자 식별자값(ID)
//                .claim(AUTHORIZATION_KEY, role.name()) // 사용자 권한
                .claim("role", role.name())
                .setExpiration(expiration) // 만료 시간
                .setIssuedAt(date) // 발급일
                .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                .compact();
        // JWT 생성 로그
        logger.info("JWT 생성 - username: {}, role: {}, 만료 시간(exp): {}", username, role, new Date(date.getTime() + TOKEN_TIME));

        return token;
    }


    // JWT Cookie 에 저장
    public void addJwtToCookie(String token, HttpServletResponse res) {
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행

            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token); // Name-Value
            cookie.setPath("/");

            // Response 객체에 Cookie 추가
            res.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }

    // JWT 토큰 substring
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            logger.info("JWT 검증 중 - 토큰: {}", token);

            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            logger.info("JWT 디코딩 성공 - 만료 시간: {}, 발급 시간: {}", claims.getBody().getExpiration(), claims.getBody().getIssuedAt());
            return true;
        } catch (ExpiredJwtException e) {
            logger.error("JWT 만료 - 만료 시간: {}, 현재 시간: {}", e.getClaims().getExpiration(), new Date());
        } catch (Exception e) {
            logger.error("JWT 검증 실패: {}", e.getMessage());
        }
        return false;
    }


    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        try {
            logger.info("JWT 디코딩 중 - 토큰: {}", token);
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            logger.error("JWT 만료  - 만료 시간: {}, 현재 시간: {}", e.getClaims().getExpiration(), new Date());
            throw e;
        }
    }

    // HttpServletRequest 에서 Cookie Value : JWT 가져오기
    public String getTokenFromRequest(HttpServletRequest req) {
        String bearerToken = req.getHeader(AUTHORIZATION_HEADER);
        return substringToken(bearerToken);
    }
}
