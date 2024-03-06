package com.example.demo.domain.chat.service;

import com.example.demo.domain.chat.dto.request.ChatRoomCreateRequest;
import com.example.demo.domain.chat.dto.request.ChatRoomDeleteRequest;
import com.example.demo.domain.chat.dto.request.ChatRoomInviteRequest;
import com.example.demo.domain.chat.dto.response.*;
import com.example.demo.domain.chat.entity.ChatMessage;
import com.example.demo.domain.chat.entity.ChatRoom;
import com.example.demo.domain.chat.entity.ChatRoomUser;
import com.example.demo.domain.chat.repository.ChatMessageJpaRepository;
import com.example.demo.domain.chat.repository.ChatRoomJpaRepository;
import com.example.demo.domain.chat.repository.ChatRoomUserJpaRepository;
import com.example.demo.domain.user.dto.response.UserInfoResponseDTO;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ExceptionCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class ChatRoomService {

    private final UserRepository userRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final ChatRoomUserJpaRepository chatRoomUserJpaRepository;
    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String,Object> chatRedisTemplate;
    private final ChannelTopic channelTopic;

    public ChatRoomListResponseDto getAllChatRoomByUserId(long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_USER));

        List<ChatRoomUser> chatRoomUserList = chatRoomUserJpaRepository.findAllWithChatRoomByUser(user);

        List<SimpleChatRoomResponseDto> simpleChatRoomResponseDtoList = chatRoomUserList.stream()
                .map(cru -> {
                    //  각 채팅방의 이름과 최신 메시지의 내용, 발신시간을 반환 (SimpleChatRoomResponseDto)
                    ChatMessage chatMessage = chatMessageJpaRepository.findTop1ByChatRoomOrderByCreatedAtDesc(cru.getChatRoom());
                    return new SimpleChatRoomResponseDto(chatMessage.getContent(), chatMessage.getCreatedAt(), cru.getChatRoom().getChatRoomName(), cru.getChatRoom().getId());
                })
                .collect(Collectors.toList());

        return new ChatRoomListResponseDto(simpleChatRoomResponseDtoList);
    }

    public ChatRoomDto getChatRoomWithUserListByChatRoomId(long chatRoomId) {
        ChatRoom chatRoom = chatRoomJpaRepository.findChatRoomWithUsers(chatRoomId);

        List<UserInfoResponseDTO> dtoList = chatRoom.getChatRoomUserList()
                .stream()
                .map(ChatRoomUser::getUser)
                .map(UserInfoResponseDTO::from)
                .collect(Collectors.toList());

        return new ChatRoomDto(chatRoom.getId(), chatRoom.getChatRoomName(), dtoList);
    }

    public ChatRoomCreateResponseDto createChatRoom(ChatRoomCreateRequest req) {

        ChatRoom chatRoomJpa = chatRoomJpaRepository.save(new ChatRoom(req.getChatRoomName()));

        User inviter = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_USER));

        List<User> userList = userRepository.findAllById(req.getUserIdList());

        if(!userList.contains(inviter)) {
            userList.add(inviter);
        }

        String invitedUsers = userList.stream()
                .filter(user -> !user.equals(inviter)) // inviter를 제외한 사용자만 필터링
                .map(User::getUserName)
                .collect(Collectors.joining(", "));

        List<ChatRoomUser> chatRoomUserList = userList.stream()
                .map(user -> new ChatRoomUser(user, chatRoomJpa))
                .collect(Collectors.toList());

        ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                .chatRoomId(chatRoomJpa.getId())
                .userId(inviter.getId())
                .userName(inviter.getUserName())
                .content(inviter.getUserName() + " 님이 " + invitedUsers + " 님을 초대했습니다.")
                .type(ChatMessage.MessageType.ENTER)
                .createdAt(LocalDateTime.now())
                .build();

        ChatMessage chatMessageEntity = chatMessageDto.toEntity(chatRoomJpa);

        chatRoomUserJpaRepository.saveAll(chatRoomUserList);
        chatMessageJpaRepository.save(chatMessageEntity);
        try {
            chatRedisTemplate.convertAndSend(channelTopic.getTopic(), objectMapper.writeValueAsString(chatMessageDto));
        } catch (JsonProcessingException e) {
            throw new CustomException(ExceptionCode.JSON_PARSING_ERROR);
        }

        return new ChatRoomCreateResponseDto(chatRoomJpa.getId(), chatRoomJpa.getChatRoomName(), req.getUserIdList());
    }

    public ChatRoomDeleteResponseDto exitChatroom(Long chatRoomId, ChatRoomDeleteRequest req)   {

        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_USER));

        ChatRoom chatRoom = chatRoomJpaRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_CHAT_ROOM));

        ChatRoomUser chatRoomUser = chatRoomUserJpaRepository.findByUserAndChatRoom(user, chatRoom)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_CHAT_ROOM_USER));

        ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                .chatRoomId(chatRoom.getId())
                .userId(user.getId())
                .userName(user.getUserName())
                .content(user.getUserName() + " 님이 퇴장했습니다.")
                .type(ChatMessage.MessageType.QUIT)
                .createdAt(LocalDateTime.now())
                .build();

        ChatMessage chatMessageEntity = chatMessageDto.toEntity(chatRoom);

        chatRoomUserJpaRepository.delete(chatRoomUser);
        chatMessageJpaRepository.save(chatMessageEntity);
        try {
            chatRedisTemplate.convertAndSend(channelTopic.getTopic(), objectMapper.writeValueAsString(chatMessageDto));
        } catch (JsonProcessingException e) {
            throw new CustomException(ExceptionCode.JSON_PARSING_ERROR);
        }

        return new ChatRoomDeleteResponseDto(chatRoomUser.getId());
    }

    public ChatRoomInviteResponseDto inviteChatRoom(ChatRoomInviteRequest req) {
        User inviter = userRepository.findById(req.getInviterId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_USER));

        User target = userRepository.findById(req.getTargetId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_USER));

        ChatRoom chatRoom = chatRoomJpaRepository.findById(req.getChatRoomId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_CHAT_ROOM));

        if(chatRoomUserJpaRepository.existsByUserAndChatRoom(target, chatRoom)) {
            throw new CustomException(ExceptionCode.ALREADY_EXIST_CHAT_ROOM_USER);
        }

        ChatRoomUser chatRoomUser = new ChatRoomUser(target, chatRoom);

        ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                .chatRoomId(chatRoom.getId())
                .userId(inviter.getId())
                .userName(inviter.getUserName())
                .content(inviter.getUserName() + " 님이 " + target.getUserName() + " 님을 초대했습니다.")
                .type(ChatMessage.MessageType.ENTER)
                .createdAt(LocalDateTime.now())
                .build();

        ChatMessage chatMessage = chatMessageDto.toEntity(chatRoom);

        chatRoomUserJpaRepository.save(chatRoomUser);
        chatMessageJpaRepository.save(chatMessage);
        try {
            chatRedisTemplate.convertAndSend(channelTopic.getTopic(), objectMapper.writeValueAsString(chatMessageDto));
        } catch (JsonProcessingException e) {
            throw new CustomException(ExceptionCode.JSON_PARSING_ERROR);
        }

        return new ChatRoomInviteResponseDto(inviter.getId(), target.getId(), chatRoom.getId());
    }

}
