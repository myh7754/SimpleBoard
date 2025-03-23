package est.wordwise.domain.security.repository;


import est.wordwise.domain.security.entity.Member;
import est.wordwise.domain.security.entity.RefreshToken;

import java.util.Optional;

public interface TokenRepository  {
    RefreshToken save(Member member, String token);
    Optional<RefreshToken> findValidRefTokenByToken(String token);
    Optional<RefreshToken> findvalidRefTokenByMemberId(Long memberId);
    RefreshToken appendBlackList(RefreshToken token);
    RefreshToken appendBlackList(String token);
}
