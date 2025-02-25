package est.wordwise.domain.security.controller;

import est.wordwise.domain.security.dto.SignupReq;
import est.wordwise.domain.security.service.AuthService;
import est.wordwise.domain.security.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;
    private final MemberService memberService;

    @PostMapping("/signup")
    public String signup(@RequestBody SignupReq req) throws Exception {
        authService.signup(req);
        return "ok";
    }

    @PostMapping("check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestBody Map<String, String> req) {
        log.info("received checkEmailInfo: {}", req);
        String email = req.get("email");
        return ResponseEntity.ok().body(memberService.checkEmail(email));
    }

    @PostMapping("check-nickname")
    public ResponseEntity<Boolean> checkNickname(@RequestBody Map<String, String> req) {
        log.info("received checkNicknameInfo: {}", req);
        String nickname = req.get("nickname");
        return ResponseEntity.ok().body(memberService.checkNickname(nickname));
    }

}
