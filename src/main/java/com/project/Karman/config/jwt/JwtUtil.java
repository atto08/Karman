package com.project.Karman.config.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    //    @Value("${spring.jwt.secret}")
//    private String SECRET_KEY;
//
//    @Value("${spring.jwt.expiration}")
//    private long EXPIRATION_TIME; // 현재 1분 1시간
    private static final String SECRET_KEY = "53q5iuVxAdB13QgL6opTWMkxOwMjSp6t1/9apIm2ey9+i3E8NzJC307RuUlv2v/ZrmMVRHkYoghWswgJC0Feiw=="; // 나중에 환경변수로!
    private static final long EXPIRATION_TIME = 1000 * 60; //* 60; // 1시간

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String validateAndGetSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}