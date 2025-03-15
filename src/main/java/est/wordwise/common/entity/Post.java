package est.wordwise.common.entity;

import est.wordwise.domain.post.dto.CreatePostRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;



    @Builder
    private Post(String title, String content, Member author) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.createAt = LocalDateTime.now();
    }

    public static Post toEntity(CreatePostRequest create, Member author) {
        return Post.builder()
                .author(author)
                .title(create.getTitle())
                .content(create.getContent())
                .build();
    }


}
