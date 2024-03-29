package com.example.demo.domain.chat.service;

import com.example.demo.domain.chat.dto.request.*;
import com.example.demo.domain.chat.dto.response.*;
import com.example.demo.domain.chat.entity.ChatMessage;
import com.example.demo.domain.chat.entity.ChatRoom;
import com.example.demo.domain.chat.entity.ChatRoomInviteCode;
import com.example.demo.domain.chat.entity.ChatRoomUser;
import com.example.demo.domain.chat.repository.ChatMessageJpaRepository;
import com.example.demo.domain.chat.repository.ChatRoomInviteCodeRepository;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@RequiredArgsConstructor
@Service
@Transactional
public class ChatRoomService {

    private final UserRepository userRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final ChatRoomUserJpaRepository chatRoomUserJpaRepository;
    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final ChatRoomInviteCodeRepository chatRoomInviteCodeRepository;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String,Object> chatRedisTemplate;
    private final ChannelTopic groupChannelTopic;

    @PreAuthorize("@userGuard.check(#userId)")
    @Transactional(readOnly = true)
    public ChatRoomListResponseDto getAllChatRoomByUserId(long userId) {

        User user = getUserById(userId);

        List<ChatRoomUser> chatRoomUserList = chatRoomUserJpaRepository.findAllWithChatRoomByUser(user);

        List<SimpleChatRoomResponseDto> simpleChatRoomResponseDtoList = chatRoomUserList.stream()
                .map(cru -> {
                    //  각 채팅방의 이름과 최신 메시지의 내용, 발신시간을 반환 (SimpleChatRoomResponseDto)
                    ChatMessage chatMessage = chatMessageJpaRepository.findTop1ByChatRoomOrderByCreatedAtDesc(cru.getChatRoom());
                    return new SimpleChatRoomResponseDto(chatMessage.getContent(), chatMessage.getCreatedAt(), cru.getChatRoom().getChatRoomName(), cru.getChatRoom().getId());
                })
                .sorted(comparing(SimpleChatRoomResponseDto::getLastMessageTime).reversed())
                .collect(Collectors.toList());

        return new ChatRoomListResponseDto(simpleChatRoomResponseDtoList);
    }

    @PreAuthorize("@chatRoomGuard.check(#chatRoomId)")
    @Transactional(readOnly = true)
    public ChatRoomDto getChatRoomWithUserListByChatRoomId(long userId, long chatRoomId) {

        User user = getUserById(userId);

        ChatRoom chatRoom = getChatRoomWithUserList(chatRoomId);

        if(chatRoom.getChatRoomUserList().stream()
                .noneMatch(chatRoomUser -> chatRoomUser.getUser().equals(user))) {
            throw new CustomException(ExceptionCode.NOT_EXIST_CHAT_ROOM_USER);
        }

        List<UserInfoResponseDTO> dtoList = chatRoom.getChatRoomUserList()
                .stream()
                .map(ChatRoomUser::getUser)
                .map(UserInfoResponseDTO::from)
                .collect(Collectors.toList());

        return new ChatRoomDto(chatRoom.getId(), chatRoom.getChatRoomName(), dtoList);
    }

    @PreAuthorize("@userGuard.check(#req.userId)")
    public ChatRoomCreateResponseDto createChatRoom(ChatRoomCreateRequest req) {

        ChatRoom chatRoomJpa = saveChatRoomToDatabase(req.getChatRoomName());

        User inviter = getUserById(req.getUserId());

        List<User> userList = userRepository.findAllById(req.getUserIdList());

        if(!userList.contains(inviter)) {
            userList.add(inviter);
        }

        String invitedUsers = userList.stream()
                .filter(user -> !user.equals(inviter)) // inviter를 제외한 사용자만 필터링
                .map(User::getUserName)
                .collect(Collectors.joining(", "));

        ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                .chatRoomId(chatRoomJpa.getId())
                .chatRoomName(chatRoomJpa.getChatRoomName())
                .userId(inviter.getId())
                .userName(inviter.getUserName())
                .content(inviter.getUserName() + " 님이 " + invitedUsers + " 님을 초대했습니다.")
                .type(ChatMessage.MessageType.ENTER)
                .createdAt(LocalDateTime.now())
                .build();

        ChatMessage chatMessage = saveChatMessageToDatabase(chatMessageDto.toEntity(chatRoomJpa));

        chatMessageDto.setChatMessageId(chatMessage.getId());

        List<ChatRoomUser> chatRoomUserList = userList.stream()
                .map(user -> new ChatRoomUser(user, chatRoomJpa))
                .collect(Collectors.toList());

        chatRoomUserJpaRepository.saveAll(chatRoomUserList);
        publishChatMessage(chatMessageDto);

        return new ChatRoomCreateResponseDto(chatRoomJpa.getId(), chatRoomJpa.getChatRoomName(), req.getUserIdList());
    }

    @PreAuthorize("@chatRoomGuard.check(#chatRoomId)")
    public ChatRoomDeleteResponseDto exitChatroom(Long chatRoomId, ChatRoomDeleteRequest req)   {

        User user = getUserById(req.getUserId());

        ChatRoom chatRoom = getChatRoomById(chatRoomId);

        ChatRoomUser chatRoomUser = getChatRoomUser(user, chatRoom);

        ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                .chatRoomId(chatRoom.getId())
                .chatRoomName(chatRoom.getChatRoomName())
                .userId(user.getId())
                .userName(user.getUserName())
                .content(user.getUserName() + " 님이 퇴장했습니다.")
                .type(ChatMessage.MessageType.QUIT)
                .createdAt(LocalDateTime.now())
                .build();

        ChatMessage chatMessageEntity = saveChatMessageToDatabase(chatMessageDto.toEntity(chatRoom));

        chatMessageDto.setChatMessageId(chatMessageEntity.getId());

        deleteChatRoomUser(chatRoomUser);

        publishChatMessage(chatMessageDto);

        return new ChatRoomDeleteResponseDto(chatRoom.getId());
    }

    @PreAuthorize("@chatRoomGuard.check(#req.chatRoomId)")
    public ChatRoomInviteResponseDto inviteChatRoom(ChatRoomInviteRequest req) {

        User inviter = getUserById(req.getInviterId());

        User target = getUserById(req.getTargetId());

        ChatRoom chatRoom = getChatRoomById(req.getChatRoomId());

        validateChatRoomInvitation(inviter, target, chatRoom);

        ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                .chatRoomId(chatRoom.getId())
                .chatRoomName(chatRoom.getChatRoomName())
                .userId(inviter.getId())
                .userName(inviter.getUserName())
                .content(inviter.getUserName() + " 님이 " + target.getUserName() + " 님을 초대했습니다.")
                .type(ChatMessage.MessageType.ENTER)
                .createdAt(LocalDateTime.now())
                .build();

        ChatMessage chatMessage = saveChatMessageToDatabase(chatMessageDto.toEntity(chatRoom));

        chatMessageDto.setChatMessageId(chatMessage.getId());

        ChatRoomUser chatRoomUser = new ChatRoomUser(target, chatRoom);
        chatRoomUser.updateFirstMessageId(chatMessage.getId());

        saveChatRoomUserToDatabase(chatRoomUser);
        publishChatMessage(chatMessageDto);

        return new ChatRoomInviteResponseDto(inviter.getId(), target.getId(), chatRoom.getId());
    }

    @PreAuthorize("@chatRoomGuard.check(#req.chatRoomId)")
    public ChatRoomInviteCodeResponseDto createInviteCode(ChatRoomInviteCodeCreateRequest req) {

        User user = getUserById(req.getUserId());

        ChatRoom chatRoom = getChatRoomById(req.getChatRoomId());

        validateUserIsChatRoomUser(user, chatRoom);

        ChatRoomInviteCode chatRoomInviteCode = createAndSaveInviteCode(chatRoom.getId(), req.getUserId());

        return new ChatRoomInviteCodeResponseDto(chatRoom.getId(), chatRoomInviteCode.getInviteCode());
    }

    @PreAuthorize("@userGuard.check(#req.userId)")
    public ChatRoomJoinResponseDto joinChatRoomByInviteCode(ChatRoomJoinRequest req) {

        ChatRoomInviteCode chatRoomInviteCode = chatRoomInviteCodeRepository.findByInviteCode(req.getInviteCode())
                .orElseThrow(() -> new CustomException(ExceptionCode.INVALID_CHAT_ROOM_INVITE_CODE));

        User user = getUserById(req.getUserId());

        User inviter = getUserById(chatRoomInviteCode.getUserId());

        ChatRoom chatRoom = getChatRoomById(chatRoomInviteCode.getChatRoomId());

        if (chatRoomUserJpaRepository.existsByUserAndChatRoom(user, chatRoom)) {
            throw new CustomException(ExceptionCode.ALREADY_EXIST_CHAT_ROOM_USER);
        }

        ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                .chatRoomId(chatRoom.getId())
                .chatRoomName(chatRoom.getChatRoomName())
                .userId(inviter.getId())
                .userName(inviter.getUserName())
                .content(inviter.getUserName() + " 님이 " + user.getUserName() + " 님을 초대했습니다.")
                .type(ChatMessage.MessageType.ENTER)
                .createdAt(LocalDateTime.now())
                .build();

        ChatMessage chatMessage = saveChatMessageToDatabase(chatMessageDto.toEntity(chatRoom));

        chatMessageDto.setChatMessageId(chatMessage.getId());

        ChatRoomUser chatRoomUser = new ChatRoomUser(user, chatRoom);
        chatRoomUser.updateFirstMessageId(chatMessage.getId());

        saveChatRoomUserToDatabase(chatRoomUser);
        publishChatMessage(chatMessageDto);

        return new ChatRoomJoinResponseDto(chatRoom.getId(), chatRoom.getChatRoomName());
    }

    private void validateUserIsChatRoomUser(User user, ChatRoom chatRoom) {
        if(!chatRoomUserJpaRepository.existsByUserAndChatRoom(user, chatRoom)) {
            throw new CustomException(ExceptionCode.NOT_EXIST_CHAT_ROOM_USER);
        }
    }

    private ChatRoomInviteCode createAndSaveInviteCode(Long chatRoomId, Long userId) {
        return chatRoomInviteCodeRepository.save(new ChatRoomInviteCode(chatRoomId, userId));
    }

    private void saveChatRoomUserToDatabase(ChatRoomUser chatRoomUser) {
        chatRoomUserJpaRepository.save(chatRoomUser);
    }

    private ChatRoom getChatRoomWithUserList(long chatRoomId) {
        return chatRoomJpaRepository.findByIdWithChatRoomUser(chatRoomId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_CHAT_ROOM));
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_USER));
    }

    private ChatRoom getChatRoomById(long chatRoomId) {
        return chatRoomJpaRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_CHAT_ROOM));
    }

    private ChatMessage saveChatMessageToDatabase(ChatMessage chatMessage) {
        return chatMessageJpaRepository.save(chatMessage);
    }

    private ChatRoom saveChatRoomToDatabase(String chatRoomName) {
        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomName(chatRoomName)
                .build();
        return chatRoomJpaRepository.save(chatRoom);
    }

    private void deleteChatRoomUser(ChatRoomUser chatRoomUser) {
        chatRoomUserJpaRepository.delete(chatRoomUser);
    }

    private void publishChatMessage(ChatMessageDto chatMessageDto) {
        try {
            chatRedisTemplate.convertAndSend(groupChannelTopic.getTopic(), objectMapper.writeValueAsString(chatMessageDto));
        } catch (JsonProcessingException e) {
            throw new CustomException(ExceptionCode.JSON_PARSING_ERROR);
        }
    }

    private ChatRoomUser getChatRoomUser(User user , ChatRoom chatRoom) {
        return chatRoomUserJpaRepository.findByUserAndChatRoom(user, chatRoom)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_CHAT_ROOM_USER));
    }

    private void validateChatRoomInvitation(User inviter, User target, ChatRoom chatRoom) {
        if(!chatRoomUserJpaRepository.existsByUserAndChatRoom(inviter, chatRoom)) {
            throw new CustomException(ExceptionCode.NOT_EXIST_CHAT_ROOM_USER);
        }

        if(chatRoomUserJpaRepository.existsByUserAndChatRoom(target, chatRoom)) {
            throw new CustomException(ExceptionCode.ALREADY_EXIST_CHAT_ROOM_USER);
        }
    }
}
