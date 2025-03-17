package est.wordwise.domain.security.config;

import est.wordwise.common.entity.Member;
import est.wordwise.common.entity.RefreshToken;
import est.wordwise.domain.security.dto.KeyPair;
import est.wordwise.domain.security.dto.MemberDetails;
import est.wordwise.domain.security.service.JwtTokenProvider;
import est.wordwise.domain.security.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.HashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final JwtConfig jwtConfig;
    @Value("${custom.front.redirect-url}")
    String targetUrl;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 인증 성공했을 때
        // oauth2.0 성공시

        // 여기서 있는 Authentication은
        // Spring Security Context Holder의 SecurityContext Authentication에서 저장된 정보
        String accessToken;
        String refresh;

        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();

        Member member = memberService.getMemberById(memberDetails.getId());

        log.info("memberDetails id : {}", memberDetails.getId());
        RefreshToken refreshToken = jwtTokenProvider.validateRefreshToken(memberDetails.getId());

        if(refreshToken == null) {
            //리프레시 토큰이 없다면 첫 로그인임
            // refresh 토큰 발급과 accessToken을 발급해줘야함
            KeyPair keyPair = jwtTokenProvider.generateKeyPair(member);
            accessToken = keyPair.getAccessToken();
            refresh = keyPair.getRefreshToken();
        } else {
            // 리프레시 토큰은 있다면 어세스 토큰만 발급해주면 됨
            accessToken = jwtTokenProvider.issueAccessToken(member);
            refresh = refreshToken.getRefreshToken();
        }

//        // 액세스 토큰 쿠키 설정 -> 이걸 다르게 바꿔야하나?
        ResponseCookie accessCookie = jwtTokenProvider.cookieToken("accessToken", accessToken);
        response.addHeader("Set-Cookie", accessCookie.toString());

        // 리프레시 토큰 쿠키 설정
        ResponseCookie refreshCookie = jwtTokenProvider.cookieToken("refreshToken", refresh);
        response.addHeader("Set-Cookie", refreshCookie.toString());

        getRedirectStrategy().sendRedirect(request,response,targetUrl);

    }

}
