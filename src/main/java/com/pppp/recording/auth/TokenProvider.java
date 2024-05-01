package com.pppp.recording.auth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import com.pppp.recording.model.UserEntity;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenProvider {
    private static final String SECRET_KEY = "NMA8JPctFuna59f5";

    public String createAccessToken(UserEntity userEntity) {
        Date expireDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS)); // 액세스 토큰 만료 시간
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setSubject(userEntity.getUserId().toString())
                .setIssuer("ePisode app")
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .compact();
    }

    public String createRefreshToken(UserEntity userEntity) {
        Date expireDate = Date.from(Instant.now().plus(7, ChronoUnit.DAYS)); // 리프레시 토큰 만료 시간
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setSubject(userEntity.getUserId().toString())
                .setIssuer("ePisode app")
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            // 토큰이 만료된 경우
            log.debug("Token expired: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            // 다른 예외 발생 시
            log.error("Failed to validate token: {}", e.getMessage());
            return false;
        }
    }

    public String validateRefreshToken(String refreshToken, UserEntity user) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(refreshToken).getBody();
            String userId = claims.getSubject();

            // 리프레시 토큰의 만료 시간 확인
            if (claims.getExpiration().before(new Date())) {
                log.debug("Refresh token expired");
                return null;
            }

            // 새로운 액세스 토큰 생성
            return createAccessToken(user);
        } catch (ExpiredJwtException e) {
            // 리프레시 토큰이 만료된 경우
            log.debug("Refresh token expired: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            // 다른 예외 발생 시
            log.error("Failed to validate refresh token: {}", e.getMessage());
            return null;
        }
    }

    public Long vaildateAndGetUserId(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        String userIdString = claims.getSubject();
        return Long.parseLong(userIdString);
    }



    public void expireToken(String token) {
        // 만료시간을 현재 시간으로 설정하여 토큰을 만료시킴
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        Date now = new Date();
        claims.setExpiration(now);
    }
}
