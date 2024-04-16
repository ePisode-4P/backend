package com.pppp.recording.auth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import com.pppp.recording.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

    public String validateRefreshToken(String refreshToken, UserEntity user) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(refreshToken).getBody();
            String userId = claims.getSubject();

            // 새로운 액세스 토큰 생성
            return createAccessToken(user);
        } catch (Exception e) {
            // 리프레시 토큰이 유효하지 않은 경우에 대한 예외 처리
            return null;
        }
    }

    public String vaildateAndGetUserId(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
