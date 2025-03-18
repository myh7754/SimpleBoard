package est.wordwise.domain.likes.repository;

import est.wordwise.common.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface LikesRepository extends JpaRepository<Likes, Long> {
     Optional<Likes> findByMember_IdAndPost_Id(Long memberId, Long postId);
}
