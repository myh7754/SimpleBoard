package est.wordwise.domain.likes.service.likeserviceImpl;

import est.wordwise.common.entity.Likes;
import est.wordwise.domain.likes.repository.LikesRepository;
import est.wordwise.domain.likes.service.LikeService;
import est.wordwise.domain.post.service.PostService;
import est.wordwise.domain.security.dto.MemberDetails;
import est.wordwise.domain.security.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final PostService postService;
    private final LikesRepository likesRepository;
    private final MemberService memberService;


    @Override
    @Transactional
    public void toggleLikeByPostId(Long postId, Authentication authentication) {
        MemberDetails memberDetails = memberService.getMemberDetails(authentication);
        Optional<Likes> findLikes = likesRepository.findByMember_IdAndPost_Id(memberDetails.getId(), postId);
        if(findLikes.isPresent()) {
            likesRepository.delete(findLikes.get());
        } else {
            Likes entity = Likes.toEntity(postService.getPostById(postId), memberService.getMemberById(memberDetails.getId()));
            likesRepository.save(entity);
        }
    }

    @Override
    public boolean isLikeByPostId(Long postId, Authentication authentication) {
        if(authentication == null) {
            return false;
        }
        MemberDetails memberDetails = memberService.getMemberDetails(authentication);
        Optional<Likes> findLikes = likesRepository.findByMember_IdAndPost_Id(memberDetails.getId(), postId);
        if(findLikes.isPresent()) {
            return true;
        } else {
            return false;
        }
    }
}
