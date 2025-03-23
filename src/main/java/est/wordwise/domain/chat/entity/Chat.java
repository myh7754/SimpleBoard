package est.wordwise.domain.chat.entity;

import est.wordwise.domain.chat.dto.ChatMessage;
import est.wordwise.domain.security.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;
    private String content; // 채팅 내용
    private LocalDateTime timestamp; // 보낸 시간

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    public static Chat toEntity(ChatMessage chatMessage, Member sender, ChatRoom chatRoom) {
        Chat chat = new Chat();
        chat.sender = sender;
        chat.content = chatMessage.getContent();
        chat.timestamp = chatMessage.getTimestamp();
        chat.chatRoom = chatRoom;
        return chat;
    }

}
