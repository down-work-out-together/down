package com.example.demo.domain.chat.repository;

import com.example.demo.domain.chat.dto.response.ChatMessageDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Repository
public class ChatMessageRepository {

    private static final String CHAT_MESSAGE = "CHAT_MESSAGE";

    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, Long, List<ChatMessageDto>> opsHashChatMessage;

    @PostConstruct
    private void init() {
        opsHashChatMessage = redisTemplate.opsForHash();
    }

    public ChatMessageDto save(ChatMessageDto chatMessageDto) {

        //  redis에 저장되어있는 리스트를 가져와, 새로 받아온 chatMessageDto를 더하여 다시 저장한다.
        List<ChatMessageDto> chatMessageList = opsHashChatMessage.get(CHAT_MESSAGE, chatMessageDto.getChatRoomId());

        //  가져온 List가 null일때 새로운 리스트를 만든다 == 처음에 메세지를 저장할경우 리스트가 없기때문에.
        if (chatMessageList == null) {
            chatMessageList = new ArrayList<>();
        }

        chatMessageList.add(chatMessageDto);

        //  redis 의 hashes 자료구조
        //  key : CHAT_MESSAGE , filed : roomId, value : chatMessageList
        opsHashChatMessage.put(CHAT_MESSAGE, chatMessageDto.getChatRoomId(), chatMessageList);

        redisTemplate.expire(CHAT_MESSAGE, 24, TimeUnit.HOURS);
        return chatMessageDto;
    }

    public void saveAll(List<ChatMessageDto> chatMessageDtoList, Long chatRoomId) {
        opsHashChatMessage.put(CHAT_MESSAGE, chatRoomId, chatMessageDtoList);
        redisTemplate.expire(CHAT_MESSAGE, 24, TimeUnit.HOURS);
    }

    public List<ChatMessageDto> findAllMessage(Long chatRoomId) {
        return opsHashChatMessage.get(CHAT_MESSAGE, chatRoomId);
    }
}
