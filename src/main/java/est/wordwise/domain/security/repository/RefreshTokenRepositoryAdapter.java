package est.wordwise.domain.security.repository;

import est.wordwise.common.entity.Member;
import est.wordwise.common.entity.RefreshToken;
import est.wordwise.common.entity.RefreshTokenBlackList;
import est.wordwise.common.exception.RefreshTokenNotFoundException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static est.wordwise.common.exception.ExceptionHandler.REFRESH_TOKEN_NOT_FOUND_ERROR;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenRepositoryAdapter implements TokenRepository {
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenBlackListRepository refreshTokenBlackListRepository;

    private final EntityManager entityManager;

    @Override
    @Transactional
    public RefreshToken save(Member member, String token) {

        return refreshTokenRepository.save(
                RefreshToken.builder()
                        .refreshToken(token)
                        .member(member)
                        .build());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RefreshToken> findValidRefTokenByToken(String token) {
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByRefreshToken(token);

        if (refreshTokenOptional.isEmpty()) {
            return refreshTokenOptional;
        }
        RefreshToken findToken = refreshTokenOptional.get();

        boolean isBanned = isBannedRefToken(findToken);
        if ( isBanned ) {
            return Optional.empty();
        } else {
            //인증받은 깨끗한 토큰 리턴
            return refreshTokenOptional;
        }
    }

    // 위(findValidRefTokenByToken)와 같은로직을 쿼리 메서드로 한번에 불러올 수 있도록 하는 것
    @Override
    @Transactional(readOnly = true)
    public Optional<RefreshToken> findvalidRefTokenByMemberId(Long memberId) {
        return entityManager.createQuery(
                        "select rf from RefreshToken rf left join RefreshTokenBlackList rtb on rtb.refreshToken = rf where rf.member.id = :memberId and rtb.id is null"
                        , RefreshToken.class)
                .setParameter("memberId", memberId)
                .getResultStream().findFirst();
    }

    @Override
    @Transactional
    public RefreshToken appendBlackList(RefreshToken token) {
        refreshTokenBlackListRepository.save(
                RefreshTokenBlackList.builder()
                        .refreshToken(token)
                        .build()
        );
        return token;
    }
    @Override
    @Transactional
    public RefreshToken appendBlackList(String token) {
        log.info("logout : {}", token);
        RefreshToken byRefreshToken = refreshTokenRepository.findByRefreshToken(token)
                .orElseThrow(() ->new RefreshTokenNotFoundException(REFRESH_TOKEN_NOT_FOUND_ERROR));
        refreshTokenBlackListRepository.save(
                RefreshTokenBlackList.builder()
                        .refreshToken(byRefreshToken)
                        .build()
        );
        return byRefreshToken;
    }
    @Transactional(readOnly = true)
    public boolean isBannedRefToken(RefreshToken token) {
        return refreshTokenBlackListRepository.existsByRefreshToken(token);
    }


}
