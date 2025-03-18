package est.wordwise.domain.likes.service;

import org.springframework.security.core.Authentication;

public interface LikeService {
    public void toggleLikeByPostId(Long postId, Authentication authentication);
    public boolean isLikeByPostId(Long postId, Authentication authentication);
}
