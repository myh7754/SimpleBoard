package est.wordwise.domain.comment.dto;



import est.wordwise.common.entity.Member;
import est.wordwise.common.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberPostDto {
    private Member member;
    private Post post;

}
