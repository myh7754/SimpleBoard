package est.wordwise.domain.chat.service;



import est.wordwise.domain.chat.dto.ChatMessage;
import est.wordwise.domain.chat.dto.ChatRoomRequest;
import est.wordwise.domain.chat.entity.ChatRoom;

public interface ChatService {
    public ChatRoom getChatRoomById(Long chatRoomId);
    public void addChatMessage(ChatMessage chatMessage, Long chatRoomId);
    public Long createChatRoom(ChatRoomRequest chatRoomRequest);
    public void joinChatRoom(ChatMessage req, Long roomId);
    public void leaveChatRoom(ChatMessage req, Long roomId);
}
