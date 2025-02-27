package est.wordwise.domain.security.config;


import est.wordwise.domain.security.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2SuccessHandler successHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, MemberService memberService) throws Exception {
        return http
                // csrf / cors/ httpbasic/ formlogin /session
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .oauth2Login(oauth-> oauth
                                .successHandler(successHandler)
                                .userInfoEndpoint(userInfo -> userInfo.userService(memberService))
                )
                .authorizeHttpRequests(auth-> { auth
                        .requestMatchers("/**").permitAll() // reqMatchers는 허락받는 url , .permitAll은 url의 허락해줄 권한 역할
                            .anyRequest().permitAll(); // 위에서 정의되지 않은 모든 요청은 인증된 사용자만 접근 가능
                })
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
