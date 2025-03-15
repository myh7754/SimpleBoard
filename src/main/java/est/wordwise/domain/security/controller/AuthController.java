package est.wordwise.domain.security.controller;

import est.wordwise.domain.security.dto.SigninRequest;
import est.wordwise.domain.security.dto.SignupRequest;
import est.wordwise.domain.security.repository.RefreshTokenRepositoryAdapter;
import est.wordwise.domain.security.service.AuthService;
import est.wordwise.domain.security.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final MemberService memberService;
    private final RefreshTokenRepositoryAdapter refreshTokenRepositoryAdapter;

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequest req) throws Exception {
        authService.signup(req);
        return "ok";
    }

    @PostMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestBody Map<String, String> req) {
        log.info("received checkEmailInfo: {}", req);
        String email = req.get("email");
        return ResponseEntity.ok().body(memberService.checkEmail(email));
    }

    @PostMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNickname(@RequestBody Map<String, String> req) {
        log.info("received checkNicknameInfo: {}", req);
        String nickname = req.get("nickname");
        return ResponseEntity.ok().body(memberService.checkNickname(nickname));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody SigninRequest req, HttpServletResponse res) throws Exception {
        log.info("received loginInfo: {}", req);
        try {
            authService.login(req,res);
            log.info("login success");
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/refresh/logout")
    public ResponseEntity<?> logout(@CookieValue(name = "refreshToken", required = false) String refreshToken
        ,HttpServletResponse response
    ) throws Exception {
        return authService.logout(refreshToken, response);
    }
//        tokenRepository.appendBlackList();


    @PostMapping("/check-auth")
    public ResponseEntity<?> checkAuthentication(Authentication authentication) throws Exception {
        log.info("확인요청");
        return authService.loginCheck(authentication);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken, HttpServletResponse res
    ) throws Exception {
        log.info("재발급 요청");
        return authService.reIssueToken(refreshToken,res);
    }

}
