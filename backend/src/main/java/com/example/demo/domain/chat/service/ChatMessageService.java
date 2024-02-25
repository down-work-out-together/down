package com.example.demo.domain.chat.service;

import com.example.demo.domain.chat.dto.response.ChatMessageDto;
import com.example.demo.domain.chat.dto.request.ChatMessageCreateRequest;
import com.example.demo.domain.chat.dto.request.ChatMessageReadCondition;
import com.example.demo.domain.chat.dto.response.ChatMessageReadResponseDto;
import com.example.demo.domain.chat.entity.ChatMessage;
import com.example.demo.domain.chat.entity.ChatRoom;
import com.example.demo.domain.chat.repository.*;
import com.example.demo.domain.user.entity.UserEntity;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.example.demo.domain.chat.repository.ChatRoomRepository.getTopic;

@RequiredArgsConstructor
@Service
@Transactional
public class ChatMessageService {

    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final ChatRoomUserJpaRepository chatRoomUserJpaRepository;
    private final UserRepository userRepository;
    private final RedisPublisher redisPublisher;

    public void saveChatMessage(ChatMessageCreateRequest req) {

        UserEntity user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_USER));

        ChatRoom chatRoom = chatRoomJpaRepository.findById(req.getChatRoomId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_CHAT_ROOM));

        if (!chatRoomUserJpaRepository.existsByUserAndChatRoom(user, chatRoom)) {
            throw new CustomException(ExceptionCode.NOT_EXIST_CHAT_ROOM_USER);
        }

        ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                .chatRoomId(req.getChatRoomId())
                .userName(user.getUserName())
                .userId(req.getUserId())
                .content(req.getContent())
                .type(req.getType())
                .createdAt(LocalDateTime.now())
                .build();

        String chatRoomIdStr = String.valueOf(req.getChatRoomId());

        ChatMessage chatMessage = chatMessageDto.toEntity(chatRoom);
        chatMessageJpaRepository.save(chatMessage); //  MySQL에 저장
        redisPublisher.messagePublish(getTopic(chatRoomIdStr), chatMessageDto);  //  webSocket에 메세지 발행
    }

    /**
     *  cond.lastMessageId 값이 없으면 jpaRepository에서 가장 최근에 생성된 20개만 불러옴.
     *  cond.lastMessageId 값이 있다면 그 이전 메시지 20개를 가져옴.
     */
    public ChatMessageReadResponseDto findLatestMessage(ChatMessageReadCondition cond) {
        return ChatMessageReadResponseDto.toDto(chatMessageJpaRepository.findLatestMessages(cond.getChatRoomId(), cond.getLastChatMessageId(), Pageable.ofSize(cond.getSize())));
    }
}
