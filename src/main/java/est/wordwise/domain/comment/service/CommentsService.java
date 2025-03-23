package est.wordwise.domain.comment.service;


import est.wordwise.domain.comment.entity.Comment;
import est.wordwise.domain.comment.dto.CommentReq;
import est.wordwise.domain.comment.dto.CommentResponse;
import est.wordwise.domain.comment.dto.CommentUpdateReq;
import est.wordwise.domain.comment.dto.MemberPostDto;

import java.util.List;

public interface CommentsService {
    public Comment findCommentsById(Long id);
    public List<Comment> findCommentsByPostId(Long postId);
    public List<CommentResponse> readCommentsByPostId(Long postId);
    public Comment createComment(CommentReq comments);
    public void updateComment(CommentUpdateReq comments);
    public Comment deleteComment(Long comments);
    public MemberPostDto findMemberAndPost(Long postId, Long memberId);
}
