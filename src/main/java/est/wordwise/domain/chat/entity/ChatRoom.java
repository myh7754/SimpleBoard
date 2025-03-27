package est.wordwise.domain.chat.entity;

import est.wordwise.domain.chat.dto.ChatMessage;
import est.wordwise.domain.chat.dto.ChatRoomRequest;
import est.wordwise.domain.chat.dto.ChatRoomResponse;
import est.wordwise.domain.security.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY)
    private List<UserChatRoom> participants = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Chat> chats = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    private Chat lastMessage;

    public void updateLastChat(Chat chat) {
        this.lastMessage = chat;
    }

}
