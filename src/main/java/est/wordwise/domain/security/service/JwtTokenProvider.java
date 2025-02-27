package est.wordwise.domain.security.service;

import est.wordwise.common.entity.Member;
import est.wordwise.domain.security.config.JwtConfig;
import est.wordwise.domain.security.dto.TokenBody;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtConfig jwtConfig;

    // 토큰생성
    public String generateJwtToken(Member member) {
        return Jwts.builder()
                .subject(member.getId().toString())
                .claim("role", member.getRole()) // 토큰에 들어갈 정보
                .issuedAt(new Date()) // 토큰 생성시간
                .expiration(new Date(new Date().getTime() + jwtConfig.getValidation().getAccess())) // 현시간에서 24시를 더한 후 만료
                .signWith(getSecretKey(), Jwts.SIG.HS256) // hs256 알고리즘과 주입된 jwtSecret을 이용하여 토큰 서명 후 compact로 토큰 문자열 최종 생성
                .compact();
    }

    // 비밀키 생성
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtConfig.getSecret().getAppKey().getBytes());
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey()) // 검증용 키 설정
                    .build()
                    .parseSignedClaims(token); // jwt 토큰을 파싱하여 서명된 클레임(payload)을 추출하는 메서드
            return true;
        } catch (JwtException e) {
            log.info("Invalid JWT Token Detected. msg = {}", e.getMessage());
            log.info("TOKEN : {}", token);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims String is empty = {}", e.getMessage());
        } catch (Exception e) {
            log.error("an error occurred while validating the token. err msg = {}", e.getMessage());
        }
        return false;
    }
    // 검증 후 토큰 추출
    public TokenBody parseJwt(String token) {
        // jws질문 jwts 질문
        Jws<Claims> parsed = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token);

        String memberId = parsed.getPayload().getSubject();
        Object role = parsed.getPayload().get("role");

        return new TokenBody(
                Long.parseLong(memberId),
                role.toString()
        );
    }
}
