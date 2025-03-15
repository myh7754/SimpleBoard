package est.wordwise.domain.security.config;


import est.wordwise.domain.security.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2SuccessHandler successHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, MemberService memberService) throws Exception {
        return http
                // csrf / cors/ httpbasic/ formlogin /session
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) )
                .oauth2Login(oauth-> oauth
                                .successHandler(successHandler)
                                .userInfoEndpoint(userInfo -> userInfo.userService(memberService))
                )
                .authorizeHttpRequests(auth-> { auth
                        .requestMatchers("/","/oauth2/authorization/**","/api/auth/**","/api/posts/**").permitAll()
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/posts/**/comments",
                                "/api/posts"
                        ).permitAll()
                         // reqMatchers는 허락받는 url , .permitAll은 url의 허락해줄 권한 역할
                        .anyRequest().authenticated(); // 위에서 정의되지 않은 모든 요청은 인증된 사용자만 접근 가능
                })
                .exceptionHandling(
                        exception -> exception.authenticationEntryPoint(
                                (req, resp, ex) -> {
                                    resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                    resp.setContentType("application/json");
                                    resp.getWriter().write("{\"error\": \"Unauthorized\"}");
                                }
                        )
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
