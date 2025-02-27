package est.wordwise.domain.security.config;

import est.wordwise.domain.security.dto.MemberDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${custom.front.redirect-url}")
    String targetUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 인증 성공했을 때
        // oauth2.0 성공시
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        log.info("Success memberDetails: {}", memberDetails.getName());


        getRedirectStrategy().sendRedirect(request,response,targetUrl);

    }
}
