package com.example.demo.domain.chat.dto.response;

import com.example.demo.domain.chat.entity.ChatMessage;
import com.example.demo.domain.chat.entity.ChatRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@Schema(description = "채팅 메시지 응답")
public class ChatMessageDto {

    @Schema(description = "채팅방 ID", example = "1")
    private Long chatRoomId;

    @Schema(description = "회원 ID", example = "1")
    private Long userId;

    @Schema(description = "회원 이름", example = "John")
    private String userName;

    @Schema(description = "메시지 내용", example = "안녕하세요.")
    private String content;

    @Schema(description = "메시지 타입 (ENTER, QUIT, TALK)", example = "ENTER")
    private ChatMessage.MessageType type;

    @Schema(description = "메시지 작성 시간")
    private LocalDateTime createdAt;

    public ChatMessage toEntity(ChatRoom chatRoom) {
        return new ChatMessage(
                this.userId,
                this.userName,
                this.content,
                this.getType(),
                chatRoom);
    }

    public ChatMessageDto(Long chatRoomId, Long userId, String userName, String content, ChatMessage.MessageType type, LocalDateTime createdAt) {
        this.chatRoomId = chatRoomId;
        this.userId = userId;
        this.userName = userName;
        this.content = content;
        this.type = type;
        this.createdAt = createdAt;
    }
}
