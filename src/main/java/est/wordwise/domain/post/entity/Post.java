package est.wordwise.domain.post.entity;

import est.wordwise.domain.comment.entity.Comment;
import est.wordwise.domain.likes.entity.Likes;
import est.wordwise.domain.post.dto.CreatePostRequest;
import est.wordwise.domain.post.dto.UpdatePostRequest;
import est.wordwise.domain.security.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post {
    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author;
    private LocalDateTime createAt;

    @Setter
    private long viewCount;

    @OneToMany(mappedBy = "post",  cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Likes> likes;

    @Builder
    private Post(String title, String content, Member author) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.createAt = LocalDateTime.now();
        this.viewCount = 0;
    }

    public static Post toEntity(CreatePostRequest create, Member author) {
        return Post.builder()
                .author(author)
                .title(create.getTitle())
                .content(create.getContent())
                .build();
    }

    public long getCommentCount() {
        return comments.size();
    }

    public long getLikeCount() {
        return likes.size();
    }

    public void update(UpdatePostRequest update) {
        this.content = update.getContent();
        this.title = update.getTitle();
    }

}
