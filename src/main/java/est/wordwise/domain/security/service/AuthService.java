package est.wordwise.domain.security.service;

import est.wordwise.common.entity.Member;
import est.wordwise.common.entity.RefreshToken;
import est.wordwise.common.exception.InvalidUsernamePasswordException;
import est.wordwise.common.repository.MemberRepository;
import est.wordwise.domain.security.dto.KeyPair;
import est.wordwise.domain.security.dto.SigninReq;
import est.wordwise.domain.security.dto.SignupReq;
import est.wordwise.domain.security.dto.TokenBody;
import est.wordwise.domain.security.repository.RefreshTokenRepositoryAdapter;
import est.wordwise.domain.security.repository.TokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static est.wordwise.common.exception.ExceptionHandler.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository refreshTokenRepositoryAdapter;

    @Transactional
    public void signup(SignupReq req) throws Exception {
        req.setPassword(passwordEncoder.encode(req.getPassword()));
        memberRepository.save(Member.toEntity(req));
    }

    @Transactional
    public void login(SigninReq req, HttpServletResponse response) {
        Member member = memberService.getMemberByEmail(req.getEmail());
        if (!passwordEncoder.matches(req.getPassword(), member.getPassword())) {
            throw new InvalidUsernamePasswordException(INVALID_USERNAME_PASSWORD_ERROR);
        }
        KeyPair keyPair = jwtTokenProvider.generateKeyPair(member);
        String accessToken = keyPair.getAccessToken();
        String refresh = keyPair.getRefreshToken();
        ResponseCookie accessCookie = jwtTokenProvider.cookieToken("accessToken", accessToken);
        ResponseCookie refreshCookie = jwtTokenProvider.cookieToken("refreshToken", refresh);
        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());
    }

    public ResponseEntity<?> loginCheck(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        log.info("내부 로그인 체크");
        if (authentication == null || !authentication.isAuthenticated()) {
            log.info("로그인 되어있지 않은 상태");
            response.put("isAuthenticated", false);
            return ResponseEntity.ok(response);
        }
        log.info("로그인 되어있는 정보 확인 : {}", authentication);
        response.put("isAuthenticated", true);
        response.put("user", authentication.getPrincipal());
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> reIssueToken(String refreshToken, HttpServletResponse response) {

        RefreshToken validateRefreshToken = jwtTokenProvider.validateRefreshToken(refreshToken);
        log.info("현재 검증된 refreshToken: {}", validateRefreshToken);
        if (validateRefreshToken == null) {
            log.info("검증된게 없다 오류 발생");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            log.info("오류 안나고 검증된 깔끔한 토큰이다");
            TokenBody tokenBody = jwtTokenProvider.parseJwt(refreshToken);
            Member loginMember = memberService.getMemberById(tokenBody.getMemberId());
            String reIssueAccessToken = jwtTokenProvider.issueAccessToken(loginMember);
            ResponseCookie accessTokenCookie = jwtTokenProvider.cookieToken("accessToken", reIssueAccessToken);
            response.addHeader("Set-Cookie", accessTokenCookie.toString());
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }

    public ResponseEntity<?> logout(String refreshToken) {
        try {
            refreshTokenRepositoryAdapter.appendBlackList(refreshToken);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


}
