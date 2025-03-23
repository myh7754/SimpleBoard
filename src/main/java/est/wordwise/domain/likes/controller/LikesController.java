package est.wordwise.domain.likes.controller;

import est.wordwise.domain.likes.service.LikeService;
import est.wordwise.domain.security.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/likes")
public class LikesController {
    private final LikeService likeService;
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<?> readPostLikes(@PathVariable Long postId, Authentication authentication) {
        boolean likeByPostId = likeService.isLikeByPostId(postId, authentication);
        return ResponseEntity.ok(likeByPostId);
    }

    @PostMapping
    public void likeAdd(@PathVariable Long postId, Authentication authentication) {
        likeService.toggleLikeByPostId(postId, authentication);
    }
}
