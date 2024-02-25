package com.example.demo.domain.chat.repository;

import com.example.demo.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findById(Long id);

    @Query("SELECT cr FROM ChatRoom cr JOIN FETCH cr.chatRoomUserList WHERE cr.id = :chatRoomId")
    ChatRoom findChatRoomWithUsers(@Param("chatRoomId") Long chatRoomId);
}
