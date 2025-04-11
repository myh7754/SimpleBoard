package est.wordwise.domain.post.service.impl;

import est.wordwise.domain.likes.repository.LikesRepository;
import est.wordwise.domain.security.entity.Member;
import est.wordwise.domain.post.entity.Post;
import est.wordwise.common.exception.PostNotFoundException;
import est.wordwise.domain.post.dto.*;
import est.wordwise.domain.post.repository.PostRepository;
import est.wordwise.domain.post.service.PostService;
import est.wordwise.domain.security.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static est.wordwise.common.exception.ExceptionHandler.POST_NOT_FOUND_ERROR;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final MemberService memberService;
    private final LikesRepository likesRepository;


    @Override
    public PostResponse getPostByIdWithMember(Long postId) {
        return postRepository.findPostWithLikeCount(postId)
                .orElseThrow(() -> new PostNotFoundException(POST_NOT_FOUND_ERROR));
    }

    @Override
    @Transactional(readOnly = true)
    public Post getPostById(Long postId) {
        Post findPost = postRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException(POST_NOT_FOUND_ERROR)
        );
        return findPost;
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse readPost(Long postId) {
//        Post postById = getPostById(postId);
        PostResponse postViewData = getPostByIdWithMember(postId);
//        Post postById = postViewData.getPost();
//        Long likeCount = postViewData.getLikeCount();
//        String author = postViewData.getAuthorNickname();
        return postViewData;
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

//    @Override
//    @Transactional
//    public void increaseViewCount(Long postId) {
//        postRepository.findById(postId).ifPresent(post -> {
//            post.setViewCount(post.getViewCount() + 1);
//        });
//    }
}
