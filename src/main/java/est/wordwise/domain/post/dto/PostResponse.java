package est.wordwise.domain.post.dto;

import est.wordwise.domain.post.entity.Post;
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
    public Long likeCount;

    public PostResponse(String title, String author, String content,
                        LocalDateTime createAt, Long likeCount) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.createAt = createAt;
        this.likeCount = likeCount;
    }

}
