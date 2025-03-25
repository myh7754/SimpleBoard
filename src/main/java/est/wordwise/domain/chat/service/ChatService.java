package est.wordwise.domain.chat.service;



import est.wordwise.domain.chat.dto.ChatMessage;
import est.wordwise.domain.chat.dto.ChatRoomRequest;
import est.wordwise.domain.chat.entity.ChatRoom;
import org.springframework.data.domain.Page;


import java.util.List;

public interface ChatService {
    public ChatRoom getChatRoomById(Long chatRoomId);
    public Page<ChatMessage> getChatMessageByChatRoomId(Long chatRoomId, int page, int size);
    public void addChatMessage(ChatMessage chatMessage, Long chatRoomId);
    public Long createChatRoom(ChatRoomRequest chatRoomRequest);
    public void joinChatRoom(ChatMessage req, Long roomId);
    public void leaveChatRoom(ChatMessage req, Long roomId);
}
