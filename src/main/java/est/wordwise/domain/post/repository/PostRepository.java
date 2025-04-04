package est.wordwise.domain.post.repository;

import est.wordwise.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByOrderByIdDesc(Pageable pageable);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.author WHERE p.id = :postId")
    Optional<Post> findByIdWithAuthor(@Param("postId") Long postId);
}
