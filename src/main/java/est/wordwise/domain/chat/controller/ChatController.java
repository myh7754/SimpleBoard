package est.wordwise.domain.chat.controller;

import est.wordwise.domain.chat.dto.ChatMessage;
import est.wordwise.domain.chat.dto.ChatRoomRequest;
import est.wordwise.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    // 메시지 발행 및 저장
    @MessageMapping("/{chatRoomId}") // /chat주소로 발행된 메시지를
    @SendTo("/sub/{chatRoomId}") // sub/chat를 구독한 사용자에게 전달
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage, @DestinationVariable Long chatRoomId) {
        log.info("채팅 도착 {}", chatMessage);
        log.info("채팅방 번호 {}", chatRoomId);
        switch (chatMessage.getMessageType()) {
            case JOIN:
                chatMessage.setContent(chatMessage.getSender() + "님이 입장하셨습니다.");
                chatService.joinChatRoom(chatMessage, chatRoomId);
                break;
            case LEAVE:
                chatMessage.setContent(chatMessage.getSender() + "님이 나갔습니다.");
                chatService.leaveChatRoom(chatMessage, chatRoomId);  // 이 부분이 누락되어 있었습니다
                break;
            case CHAT:
                chatService.addChatMessage(chatMessage, chatRoomId);
                break;
            default:
                log.warn("지원하지 않는 메시지 타입: {}", chatMessage.getMessageType());
        }
        return chatMessage;
    }

    @PostMapping("/api/chatroom") //채팅방생성
    public ResponseEntity<?> createChatRoom(@RequestBody ChatRoomRequest chatRoomRequest) {
        log.info("채팅방 생성 요청 {}", chatRoomRequest);
        Long roomId = chatService.createChatRoom(chatRoomRequest);
        return ResponseEntity.ok(roomId);
    }

}

