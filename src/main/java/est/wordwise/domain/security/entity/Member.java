package est.wordwise.domain.security.entity;

import est.wordwise.domain.chat.entity.UserChatRoom;
import est.wordwise.domain.security.dto.SignupRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;
    @Column(nullable = false, unique = true)
    private String email;
    private String password;


    @Enumerated(EnumType.STRING)
    private Auth role;

    private String provider;
    private Boolean deleted;

    // 채팅방과의 중간 테이블 매핑
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<UserChatRoom> userChatRooms = new ArrayList<>();


    private LocalDateTime createAt;

    @Builder
    private Member(String nickname, String email,  String password, Auth role, String provider ) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.role = Auth.MEMBER;
        this.provider = provider;
        this.deleted = false;
        this.createAt = LocalDateTime.now();
    }

    public static Member toEntity(SignupRequest req) {
        Member member = new Member();
        member.nickname = req.nickname;
        member.email = req.email;
        member.password = req.password;
        member.deleted = false;
        member.role = Auth.MEMBER;
        member.createAt = LocalDateTime.now();
        member.provider = "provider";
        return member;
    }
}
