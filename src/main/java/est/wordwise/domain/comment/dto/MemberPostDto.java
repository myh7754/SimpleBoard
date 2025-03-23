package est.wordwise.domain.comment.dto;



import est.wordwise.domain.security.entity.Member;
import est.wordwise.domain.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberPostDto {
    private Member member;
    private Post post;

}
