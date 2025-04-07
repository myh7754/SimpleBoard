package est.wordwise.domain.likes.entity;

import est.wordwise.domain.post.entity.Post;
import est.wordwise.domain.security.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "likes",
        indexes = {
                @Index(name = "idx_post_id", columnList = "post_id")
        })
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likes_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static Likes toEntity(Post post, Member member) {
        Likes likes = new Likes();
        likes.post = post;
        likes.member = member;
        return likes;
    }
}
