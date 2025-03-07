package est.wordwise.domain.security.service;

import est.wordwise.common.entity.Member;
import est.wordwise.common.entity.RefreshToken;
import est.wordwise.domain.security.config.JwtConfig;
import est.wordwise.domain.security.dto.KeyPair;
import est.wordwise.domain.security.dto.TokenBody;
import est.wordwise.domain.security.repository.RefreshTokenRepository;
import est.wordwise.domain.security.repository.RefreshTokenRepositoryAdapter;
import est.wordwise.domain.security.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtConfig jwtConfig;
    private final TokenRepository refreshTokenRepositoryAdapter;

    //토큰생성
    public KeyPair generateKeyPair(Member member) {
        String accessToken = issueAccessToken(member);
        String refreshToken = issueRefreshToken(member);

        refreshTokenRepositoryAdapter.save(member, refreshToken);
        log.info("access token = {}", accessToken);
        log.info("refresh token = {}", refreshToken);
        return  KeyPair.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 토큰을 담은 쿠키 생성
    public ResponseCookie cookieToken(String key, String token) {
        String path;
        Long expired;
        if(key.equals("accessToken")) {
            path = "/";
            expired = 1500L; // 15분
        } else {
            path = "/api/refresh";
            expired = 6000L; // 1시간
        }

        return ResponseCookie.from(key, token)
                .httpOnly(true)
                .secure(false)
                .path(path)
                .sameSite("Lax")
                .maxAge(expired)
                .build();
    }

    public String issueAccessToken(Member member) {
        return issue( member, jwtConfig.getValidation().getAccess());
    }
    public String issueRefreshToken(Member member) {
        return issue( member,jwtConfig.getValidation().getRefresh());
    }

    //생성
    private String issue(Member member, Long validTime) {
        return Jwts.builder()
                .subject(member.getId().toString())
                .claim("role", member.getRole())
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + validTime))
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }

    // 비밀키 생성
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtConfig.getSecret().getAppKey().getBytes());
    }

    // 멤버 id로 리프레시 토큰 검증
    public RefreshToken validateRefreshToken(Long memberId) {
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepositoryAdapter.findvalidRefTokenByMemberId(memberId);
        return refreshTokenOptional.orElse(null);
    }
    public RefreshToken validateRefreshToken(String token) {
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepositoryAdapter.findValidRefTokenByToken(token);
        return refreshTokenOptional.orElse(null);
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
