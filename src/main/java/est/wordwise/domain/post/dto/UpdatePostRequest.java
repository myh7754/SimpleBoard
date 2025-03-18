package est.wordwise.domain.post.dto;

import lombok.Data;

@Data
public class UpdatePostRequest {
    private String title;
    private String content;
}
