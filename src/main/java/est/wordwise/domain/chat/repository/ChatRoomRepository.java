package est.wordwise.domain.chat.repository;

import est.wordwise.domain.chat.entity.ChatRoom;
import est.wordwise.domain.security.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

}
