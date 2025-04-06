package est.wordwise.domain.post.dto;

import est.wordwise.domain.post.entity.Post;

public interface PostViewProjection {
    Post getPost();
    Long getLikeCount();
    String getAuthorNickname();
}
