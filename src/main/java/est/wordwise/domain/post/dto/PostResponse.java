package est.wordwise.domain.post.dto;

import est.wordwise.common.entity.Post;
import est.wordwise.domain.comment.dto.CommentResponse;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class PostResponse {

    public String title;
    public String author;
    public String content;
    public LocalDateTime createAt;
    public String board;
    public List<CommentResponse> comments;


    public static PostResponse from(Post post) {
        PostResponse postResponse = new PostResponse();
        postResponse.title = post.getTitle();
        postResponse.content = post.getContent();
        postResponse.createAt = post.getCreateAt();
        postResponse.author = post.getAuthor().getNickname();
        return postResponse;
    }
}
