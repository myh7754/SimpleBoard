package est.wordwise.domain.comment.service.impl;



import est.wordwise.common.entity.Comment;
import est.wordwise.common.entity.Member;
import est.wordwise.common.entity.Post;
import est.wordwise.common.exception.CommentNotFoundException;
import est.wordwise.domain.comment.dto.CommentReq;
import est.wordwise.domain.comment.dto.CommentResponse;
import est.wordwise.domain.comment.dto.CommentUpdateReq;
import est.wordwise.domain.comment.dto.MemberPostDto;
import est.wordwise.domain.comment.repository.CommentsRepository;
import est.wordwise.domain.comment.service.CommentsService;
import est.wordwise.domain.post.service.PostService;
import est.wordwise.domain.security.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentsServiceImpl implements CommentsService {
    private final MemberService memberService;
    private final PostService postService;
    private final CommentsRepository commentsRepository;

    @Override
    @Transactional(readOnly = true)
    public Comment findCommentsById(Long id) {
        return commentsRepository.findById(id).orElseThrow(
                () -> new CommentNotFoundException("유효하지 않은 댓글 번호입니다.") // 나중에 에러 핸들러에 메시지 추가
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findCommentsByPostId(Long postId) {
        List<Comment> byPostId = commentsRepository.findByPostIdAndParentIsNull(postId);
        return byPostId;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> readCommentsByPostId(Long postId) {
        List<Comment> commentsByPostId = findCommentsByPostId(postId);
        return commentsByPostId.stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Comment createComment(CommentReq comments) {
        MemberPostDto findMemberAndPost = findMemberAndPost(comments.getPostId(), comments.getAuthor());
        if(comments.getParentId() == null) {
            return commentsRepository.save(Comment.toEntity(comments.getContent(),findMemberAndPost.getMember(), findMemberAndPost.getPost(), null));
        } else {
            return commentsRepository.save(Comment.toEntity(comments.getContent(),findMemberAndPost.getMember(), findMemberAndPost.getPost(), findCommentsById(comments.getParentId())));
        }
    }

    @Override
    @Transactional
    public Comment deleteComment(Long commentId) {
        // 나중에 소프트 delete or 그냥 삭제할지 결졍되면 구현
        commentsRepository.deleteById(commentId);
        return null;
    }

    @Override
    @Transactional
    public void updateComment(CommentUpdateReq comments) {
        findCommentsById(comments.getCommentId()).update(comments.getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public MemberPostDto findMemberAndPost(Long postId, Long memberId) {
        Member member = memberService.getMemberById(memberId);
        Post post = postService.getPostById(postId);
        return new MemberPostDto(member, post);
    }
}
