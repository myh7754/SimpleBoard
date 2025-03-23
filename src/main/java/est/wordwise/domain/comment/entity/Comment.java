package est.wordwise.domain.comment.entity;



import est.wordwise.domain.post.entity.Post;
import est.wordwise.domain.security.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String content;

    private LocalDateTime createDate;

    // 부모 댓글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    // 자식 대댓글
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "post_id" )
    private Post post;


    private Comment(String conent, Member member, Post post, Comment parent) {
        this.content = conent;
        this.member = member;
        this.post = post;
        this.parent = parent;
    }

    public static Comment toEntity(String conent, Member member, Post post, Comment parent) {
        Comment comments = new Comment(conent, member, post,parent);
        comments.createDate = LocalDateTime.now();
        return comments;
    }

    public void update(String content) {
        this.content = content;
    }

    public void delete() {}


}
