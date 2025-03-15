package est.wordwise.domain.comment.controller;


import est.wordwise.domain.comment.dto.CommentReq;
import est.wordwise.domain.comment.dto.CommentResponse;
import est.wordwise.domain.comment.dto.CommentUpdateReq;
import est.wordwise.domain.comment.service.CommentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.events.Comment;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
@Slf4j
public class CommentController {
    private final CommentsService commentsService;
    @GetMapping
    public String getComment(@PathVariable Long postId) {
        List<CommentResponse> commentResponses = commentsService.readCommentsByPostId(postId);
        log.info("getComment: {}", commentResponses);
        return "ok";
    }

    @PostMapping
    public String createComment(@RequestBody CommentReq req) {
        log.info("createComment: {}", req);
        commentsService.createComment(req);
        return "ok";
    }

    @PutMapping
    public String updateComment(@RequestBody CommentUpdateReq req) {
        commentsService.updateComment(req);
        return "ok";
    }

    @DeleteMapping("/comments/{id}")
    public String deleteComment(@PathVariable Long id) {
        commentsService.deleteComment(id);
        return "ok";
    }
}
