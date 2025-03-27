package est.wordwise.domain.security.entity;

import est.wordwise.domain.chat.entity.ChatRoom;
import est.wordwise.domain.chat.entity.UserChatRoom;
import est.wordwise.domain.security.dto.SignupRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
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
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserChatRoom> userChatRooms = new ArrayList<>();


    private LocalDateTime createAt;

    public List<ChatRoom> getChatRooms() {
        return userChatRooms.stream()
                .map(UserChatRoom::getChatRoom)
                .collect(Collectors.toList());
    };

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
