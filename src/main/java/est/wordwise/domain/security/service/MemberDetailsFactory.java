package est.wordwise.domain.security.service;

import est.wordwise.domain.security.dto.MemberDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

@Slf4j
public class MemberDetailsFactory {
    public static MemberDetails create(String provider, OAuth2User oauth2User) {
        log.info("oauth2User {}",oauth2User);

        Map<String, Object> attributes = oauth2User.getAttributes();
        log.info("attributes {}",attributes);
        switch (provider) {
            case "GOOGLE" -> {
                return MemberDetails.builder()
                        .name(attributes.get("name").toString())
                        .email(attributes.get("email").toString())
                        .attributes(attributes)
                        .build();
            }
            case "KAKAO" -> {
                Map<String, String> properties = (Map<String, String>) attributes.get("properties");
                log.info("properties : {}", properties);
                return MemberDetails.builder()
                        .name(properties.get("nickname"))
                        .email(attributes.get("id").toString() + "@kakao.com")
                        .attributes(attributes)
                        .build();
            }
            case "NAVER" -> {
                Map<String, String> properties = (Map<String, String>) attributes.get("response");
                return MemberDetails.builder()
                        .name(properties.get("name"))
                        .email(properties.get("email"))
                        .attributes(attributes)
                        .build();
            }
            default -> throw new IllegalArgumentException("Unknown provider: " + provider);
        }
    }
}
