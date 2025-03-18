package est.wordwise.domain.post.service;

import est.wordwise.common.entity.Post;
import est.wordwise.domain.post.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface PostService {
    public Post getPostById(Long postId);
    public PostResponse readPost(Long postId);
    public Page<PostsResponse> readPosts(int page, int size);
    public void save(CreatePostRequest post, Authentication authentication);
    public void update(Long id, UpdatePostRequest post);
    public void delete(Long postId);
    public void increaseViewCount(Long postId);

}
