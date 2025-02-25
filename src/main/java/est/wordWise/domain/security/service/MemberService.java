package est.wordwise.domain.security.service;

import est.wordwise.common.entity.Member;
import est.wordwise.common.exception.MemberNotFoundException;
import est.wordwise.common.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static est.wordwise.common.exception.ExceptionHandler.MEMBER_NOT_FOUND_ERROR;

@Service
@RequiredArgsConstructor
public class MemberService {
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

}
