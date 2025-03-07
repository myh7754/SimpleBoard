package est.wordwise.domain.security.config;

import est.wordwise.domain.security.dto.MemberDetails;
import est.wordwise.domain.security.dto.TokenBody;
import est.wordwise.domain.security.service.JwtTokenProvider;
import est.wordwise.domain.security.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveTokenFromCookie(request);
        log.info("token {}", token);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            log.info("access 토큰 쿠키 검증 성공");
            TokenBody tokenBody = jwtTokenProvider.parseJwt(token);
            MemberDetails memberDetails = memberService.loadMemberDetailById(tokenBody.getMemberId());
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    memberDetails,
                    token,
                    memberDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }


        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");//Authorization, Content-Type, Accept,Cookie, Host 등이 들어갈 수 있음
        // -> "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6..."과 같은 값으로 추출됨
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String resolveTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
