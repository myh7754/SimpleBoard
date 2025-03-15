package est.wordwise.domain.post.dto;

import est.wordwise.common.entity.Post;

import java.time.LocalDateTime;

public record PostsResponse(
        Long id,
        String title,
        String content,
        String author,
        LocalDateTime createdAt
) {
    public static PostsResponse from(Post post) {
        return new PostsResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getNickname(),
                post.getCreateAt()
        );
    }
}