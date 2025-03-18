package est.wordwise.domain.post.service.impl;

import est.wordwise.common.entity.Member;
import est.wordwise.common.entity.Post;
import est.wordwise.common.exception.PostNotFoundException;
import est.wordwise.domain.comment.service.CommentsService;
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
        PostResponse from = PostResponse.from(postById);
        return from;
    }

    @Override
    public Page<PostsResponse> readPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findAllByOrderByIdDesc(pageable)
                .map(PostsResponse::from);
    }

    @Override
    @Transactional
    public void save(CreatePostRequest post, Authentication authentication) {
        Member loginMember = memberService.getLoginMember(authentication);
        Post save = postRepository.save(Post.toEntity(post, loginMember));
    }

    @Override
    @Transactional
    public void update(Long id, UpdatePostRequest req) {
        postRepository.findById(id).ifPresent(post -> {
            post.update(req);
        });
    }

    @Override
    public void delete(Long postId) {
        postRepository.findById(postId).ifPresent(post -> {
            postRepository.delete(post);
        });
    }

    @Override
    @Transactional
    public void increaseViewCount(Long postId) {
        postRepository.findById(postId).ifPresent(post -> {
            post.setViewCount(post.getViewCount() + 1);
        });
    }
}
