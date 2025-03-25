package est.wordwise.domain.chat.repository;

import est.wordwise.domain.chat.dto.ChatMessage;
import est.wordwise.domain.chat.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface ChatMessageRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByChatRoomId(Long chatRoomId);
    Page<Chat> findByChatRoomId(Long chatRoomId, Pageable pageable);
}

