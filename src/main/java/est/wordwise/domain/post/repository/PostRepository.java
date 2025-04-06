package est.wordwise.domain.post.repository;

import est.wordwise.domain.post.dto.PostResponse;
import est.wordwise.domain.post.dto.PostViewProjection;
import est.wordwise.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByOrderByIdDesc(Pageable pageable);

    @Query("""
    SELECT NEW est.wordwise.domain.post.dto.PostResponse(
        p.title,
        a.nickname,
        p.content,
        p.createAt,
        COUNT(l.id)
    )
    FROM Post p
    LEFT JOIN p.author a
    LEFT JOIN Likes l ON l.post = p
    WHERE p.id = :postId
    GROUP BY p.id, a.nickname, p.title, p.content, p.createAt""")
    Optional<PostResponse> findPostWithLikeCount(@Param("postId") Long postId);
}


