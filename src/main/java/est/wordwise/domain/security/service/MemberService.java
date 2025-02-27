package est.wordwise.domain.security.service;

import est.wordwise.common.entity.Member;
import est.wordwise.common.exception.MemberNotFoundException;
import est.wordwise.common.repository.MemberRepository;
import est.wordwise.domain.security.dto.MemberDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static est.wordwise.common.exception.ExceptionHandler.MEMBER_NOT_FOUND_ERROR;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(
                () -> new MemberNotFoundException(MEMBER_NOT_FOUND_ERROR)
        );
    }

    public Boolean checkEmail(String email) {
        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        return memberOptional.isEmpty();
    }

    public Boolean checkNickname(String nickname) {
        Optional<Member> memberOptional = memberRepository.findByNickname(nickname);
        return memberOptional.isEmpty();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("OAuth2User {}" , oAuth2User);
        String provider = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        MemberDetails memberDetails = MemberDetailsFactory.create(provider, oAuth2User);

        Optional<Member> memberOptional = memberRepository.findByEmail(memberDetails.getEmail());
        Member findMember = memberOptional.orElseGet(
                () -> {
                    Member member = Member.builder()
                            .email(memberDetails.getEmail())
                            .nickname(memberDetails.getName())
                            .nickname("OAUTHUSER")
                            .provider(provider)
                            .build();
                    return memberRepository.save(member);
                }
        );

        if ( findMember.getProvider().equals(provider) ) {
            memberDetails.setRole(String.valueOf(findMember.getRole()));
            return memberDetails;
        } else {
            throw new RuntimeException();
        }
    }
}
