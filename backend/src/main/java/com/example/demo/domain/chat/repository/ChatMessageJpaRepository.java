package com.example.demo.domain.chat.repository;

import com.example.demo.domain.chat.dto.response.ChatMessageDto;
import com.example.demo.domain.chat.entity.ChatMessage;
import com.example.demo.domain.chat.entity.ChatRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ChatMessageJpaRepository extends JpaRepository<ChatMessage, Long> {

    ChatMessage findTop1ByChatRoomOrderByCreatedAtDesc(ChatRoom chatRoom);

    @Query("SELECT new com.example.demo.domain.chat.dto.response.ChatMessageDto(cm.id, cm.userId, cm.userName, cm.content, cm.type, cm.createdAt) FROM ChatMessage cm WHERE cm.chatRoom.id = :chatRoomId AND cm.id < :lastMessageId ORDER BY cm.id DESC")
    Slice<ChatMessageDto> findLatestMessages(@Param("chatRoomId") Long chatRoomId, @Param("lastMessageId") Long lastMessageId, Pageable pageable);
}
