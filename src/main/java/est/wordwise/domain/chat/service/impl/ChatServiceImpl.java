package est.wordwise.domain.chat.service.impl;

import est.wordwise.common.exception.ChatRoomNotFoundException;
import est.wordwise.common.exception.InvalidChatRoomCreationException;
import est.wordwise.domain.chat.dto.ChatMessage;
import est.wordwise.domain.chat.dto.ChatRoomRequest;
import est.wordwise.domain.chat.entity.Chat;
import est.wordwise.domain.chat.entity.ChatRoom;
import est.wordwise.domain.chat.entity.UserChatRoom;
import est.wordwise.domain.chat.repository.ChatMessageRepository;
import est.wordwise.domain.chat.repository.ChatRoomRepository;
import est.wordwise.domain.chat.repository.UserChatRoomRepository;
import est.wordwise.domain.chat.service.ChatService;
import est.wordwise.domain.security.entity.Member;
import est.wordwise.domain.security.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static est.wordwise.common.exception.ExceptionHandler.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserChatRoomRepository userChatRoomRepository;
    private final MemberService memberService;


    @Override
    public ChatRoom getChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId).orElseThrow(
                ()-> new ChatRoomNotFoundException(CHAT_ROOM_FOUND_ERROR)
        );
    }

    // 채팅 추가
    @Override
    public void addChatMessage(ChatMessage chatMessage, Long roomId) {
        Member memberByNickname = memberService.getMemberByNickname(chatMessage.getSender());
        ChatRoom chatRoomById = getChatRoomById(roomId);
        chatMessageRepository.save(Chat.toEntity(chatMessage, memberByNickname, chatRoomById));
    }

    // 채팅방 생성
    @Override
    public Long createChatRoom(ChatRoomRequest chatRoomRequest) {
        if (Objects.equals(chatRoomRequest.getMemberName(), chatRoomRequest.getOpponentName())) {
            throw new InvalidChatRoomCreationException(INVALID_CHAT_ROOM_CREATION_ERROR);
        }
        Member member = memberService.getMemberByNickname(chatRoomRequest.getMemberName());
        Member opponent = memberService.getMemberByNickname(chatRoomRequest.getOpponentName());

        Optional<Long> existRoom = userChatRoomRepository.findChatRoomIdsByMembers(member, opponent);

        // 만약 이미 채팅방이 생성되었있다면 해당 채팅방을 return
        if(existRoom.isPresent()) {
            return existRoom.get();
        }

        ChatRoom newChatRoom = new ChatRoom();
        chatRoomRepository.save(newChatRoom);

        userChatRoomRepository.save(UserChatRoom.toEntity(member, newChatRoom));
        userChatRoomRepository.save(UserChatRoom.toEntity(opponent, newChatRoom));
        return newChatRoom.getId();
    }


    // 멤버 추가
    @Override
    public void joinChatRoom(ChatMessage req, Long roomId) {
        ChatRoom joinRoom = getChatRoomById(roomId);
        Member joinMember = memberService.getMemberByNickname(req.getSender());
        // 이미 참여 중인지 확인
        boolean alreadyJoined = userChatRoomRepository.existsByMemberAndChatRoom(joinMember, joinRoom);
        if (!alreadyJoined) {
            userChatRoomRepository.save(UserChatRoom.toEntity(joinMember, joinRoom));
        }
    }

    // 채팅방 나가기
    @Override
    public void leaveChatRoom(ChatMessage req, Long roomId) {
        ChatRoom leaveChatRoom = getChatRoomById(roomId);
        Member leaveMember = memberService.getMemberByNickname(req.getSender());
        // 이미 참여 중인지 확인
        UserChatRoom userChatRoom = userChatRoomRepository.findByMemberAndChatRoom(leaveMember, leaveChatRoom)
                .orElseThrow(() -> new RuntimeException(USER_NOT_IN_CHAT_ROOM_ERROR));

        userChatRoomRepository.delete(userChatRoom);

    }


}
