package est.wordwise.domain.security.service;

import est.wordwise.common.entity.Comment;
import est.wordwise.common.entity.Member;
import est.wordwise.common.entity.Post;
import est.wordwise.domain.comment.service.CommentsService;
import est.wordwise.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberAuthService {
    private final CommentsService commentsService;
    private final PostService postService;
    private final MemberService memberService;

    // 댓글 작성자와 로그인된 사용자가 같다면 true 다르다면 false
    public boolean commentAuthCheck(Authentication authentication, Long commentId) {
        Comment commentsById = commentsService.findCommentsById(commentId);
        Member loginMember = memberService.getLoginMember(authentication);
        return  loginMember.getId().equals(commentsById.getMember().getId());
    }

    public boolean postAuthCheck(Authentication authentication, Long postId) {
        Post postById = postService.getPostById(postId);
        Member loginMember = memberService.getLoginMember(authentication);
        log.info("현재 사용자", loginMember);
        return loginMember.getId().equals(postById.getAuthor().getId());
    }


}
