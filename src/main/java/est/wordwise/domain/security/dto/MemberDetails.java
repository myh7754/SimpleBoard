package est.wordwise.domain.security.dto;

import est.wordwise.domain.security.entity.Member;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
@ToString
public class MemberDetails implements OAuth2User {
    @Setter
    private Long id;
    private String name;
    private String email;

    @Setter
    private String role;

    private Map<String, Object> attributes;

    public static MemberDetails from(Member member) {
        MemberDetails memberDetail = new MemberDetails();
        memberDetail.id = member.getId();
        memberDetail.name = member.getNickname();
        memberDetail.email = member.getEmail();
        memberDetail.role = member.getRole().toString();
        return memberDetail;
    }

    @Builder
    public MemberDetails(String name, String email,Map<String, Object> attributes) {
        this.name = name;
        this.email = email;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getName() {
        return name;
    }


}
