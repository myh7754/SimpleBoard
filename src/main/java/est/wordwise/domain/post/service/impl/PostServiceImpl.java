package est.wordwise.domain.post.service.impl;

import est.wordwise.common.entity.Member;
import est.wordwise.common.entity.Post;
import est.wordwise.common.exception.PostNotFoundException;
import est.wordwise.domain.post.dto.*;
import est.wordwise.domain.post.repository.PostRepository;
import est.wordwise.domain.post.service.PostService;
import est.wordwise.domain.security.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static est.wordwise.common.exception.ExceptionHandler.POST_NOT_FOUND_ERROR;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final MemberService memberService;

    @Override
    public Post getPostById(Long postId) {
        Post findPost = postRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException(POST_NOT_FOUND_ERROR)
        );
        return findPost;
    }

    @Override
    public PostResponse readPost(Long postId) {
        Post postById = getPostById(postId);
        return PostResponse.from(postById);
    }

    @Override
    public Page<PostsResponse> readPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findAllByOrderByIdDesc(pageable)
                .map(PostsResponse::from);
    }

    @Override
    @Transactional
    public ResponseEntity<?> save(CreatePostRequest post, Authentication authentication) {
        Member loginMember = memberService.getLoginMember(authentication);
        postRepository.save(Post.toEntity(post, loginMember));
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> update(UpdatePostRequest post) {
        return null;
    }

    @Override
    public ResponseEntity<?> delete(DeletePostRequest id) {
        return null;
    }
}
