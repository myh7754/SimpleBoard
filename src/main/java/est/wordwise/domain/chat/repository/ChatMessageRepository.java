package est.wordwise.domain.chat.repository;

import est.wordwise.domain.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<Chat, Long> {
}
