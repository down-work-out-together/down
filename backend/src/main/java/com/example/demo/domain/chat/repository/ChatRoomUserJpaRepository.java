package com.example.demo.domain.chat.repository;

import com.example.demo.domain.chat.entity.ChatRoom;
import com.example.demo.domain.chat.entity.ChatRoomUser;
import com.example.demo.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRoomUserJpaRepository extends JpaRepository<ChatRoomUser, Long> {

    Optional<ChatRoomUser> findByUserAndChatRoom(UserEntity user, ChatRoom chatRoom);
    boolean existsByUserAndChatRoom(UserEntity user, ChatRoom chatRoom);

    // 회원 entity로 채팅방 찾기
    @Query("select cru from ChatRoomUser cru join fetch cru.chatRoom where cru.user =: userEntity")
    List<ChatRoomUser> findAllByUser(UserEntity userEntity);

}
