package com.example.demo.domain.chat.service;

import com.example.demo.domain.chat.dto.request.ChatRoomCreateRequest;
import com.example.demo.domain.chat.dto.request.ChatRoomDeleteRequest;
import com.example.demo.domain.chat.dto.response.*;
import com.example.demo.domain.chat.entity.ChatMessage;
import com.example.demo.domain.chat.entity.ChatRoom;
import com.example.demo.domain.chat.entity.ChatRoomUser;
import com.example.demo.domain.chat.repository.ChatMessageJpaRepository;
import com.example.demo.domain.chat.repository.ChatRoomJpaRepository;
import com.example.demo.domain.chat.repository.ChatRoomUserJpaRepository;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public ChatRoomListResponseDto getAllChatRoomByUserId(long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_USER));

        List<ChatRoomUser> chatRoomUserList = chatRoomUserJpaRepository.findAllByUser(user);

        List<SimpleChatRoomResponseDto> simpleChatRoomResponseDtoList = chatRoomUserList.stream()
                .map(cru -> {
                    ChatMessage chatMessage = chatMessageJpaRepository.findTop1ByChatRoomOrderByCreatedAtDesc(cru.getChatRoom());
                    return new SimpleChatRoomResponseDto(chatMessage.getContent(), chatMessage.getCreatedAt(), cru.getChatRoom().getChatRoomName());
                })
                .collect(Collectors.toList());

        return new ChatRoomListResponseDto(simpleChatRoomResponseDtoList);
    }

    public ChatRoomDto getChatRoomWithUserListByChatRoomId(long chatRoomId) {
        ChatRoom chatRoom = chatRoomJpaRepository.findChatRoomWithUsers(chatRoomId);

        List<Long> userIdList = chatRoom.getChatRoomUserList()
                .stream()
                .map(ChatRoomUser::getUser)
                .map(User::getId)
                .collect(Collectors.toList());

        return new ChatRoomDto(chatRoom.getId(), chatRoom.getChatRoomName(), userIdList);
    }

    public ChatRoomCreateResponseDto createChatRoom(ChatRoomCreateRequest chatRoomCreateRequest) {

        ChatRoom chatRoomJpa = chatRoomJpaRepository.save(new ChatRoom(chatRoomCreateRequest.getChatRoomName()));

        List<User> userList = userRepository.findAllById(chatRoomCreateRequest.getUserIdList());

        List<ChatRoomUser> chatRoomUserList = userList.stream()
                .map(user -> new ChatRoomUser(user, chatRoomJpa))
                .collect(Collectors.toList());

        chatRoomUserJpaRepository.saveAll(chatRoomUserList);

        return new ChatRoomCreateResponseDto(chatRoomJpa.getId(), chatRoomJpa.getChatRoomName(), chatRoomCreateRequest.getUserIdList());
    }

    public ChatRoomDeleteResponseDto exitChatroom(ChatRoomDeleteRequest req) {

        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_USER));

        ChatRoom chatRoom = chatRoomJpaRepository.findById(req.getChatRoomId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_CHAT_ROOM));

        ChatRoomUser chatRoomUser = chatRoomUserJpaRepository.findByUserAndChatRoom(user, chatRoom)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_CHAT_ROOM_USER));

        chatRoomUserJpaRepository.delete(chatRoomUser);

        return new ChatRoomDeleteResponseDto(chatRoomUser.getId());
    }

}
