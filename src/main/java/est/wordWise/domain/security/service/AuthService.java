package est.wordwise.domain.security.service;

import est.wordwise.common.entity.Member;
import est.wordwise.common.repository.MemberRepository;
import est.wordwise.domain.security.dto.SignupReq;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(SignupReq req) throws Exception {
        req.setPassword(passwordEncoder.encode(req.getPassword()));
        memberRepository.save(Member.toEntity(req));
    }


}
