package est.wordwise.domain.security.repository;

import est.wordwise.common.entity.RefreshToken;
import est.wordwise.common.entity.RefreshTokenBlackList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenBlackListRepository extends JpaRepository<RefreshTokenBlackList, Long> {
    boolean existsByRefreshToken(RefreshToken refreshToken);
}
