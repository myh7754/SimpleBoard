package est.wordwise.domain.post.controller;


import est.wordwise.domain.post.dto.CreatePostRequest;
import est.wordwise.domain.post.dto.PostResponse;
import est.wordwise.domain.post.dto.PostsResponse;
import est.wordwise.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<?> postDetail(@PathVariable Long id) {
        PostResponse responseEntity = postService.readPost(id);
        return ResponseEntity.ok(responseEntity);
    }

    @GetMapping
    public ResponseEntity<?> index(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10")int size
    ) {
        Page<PostsResponse> responseEntity = postService.readPosts(page, size);
        return ResponseEntity.ok(responseEntity);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody CreatePostRequest req, Authentication authentication) {
        postService.save(req, authentication);
        return ResponseEntity.ok().build();
    }
}
