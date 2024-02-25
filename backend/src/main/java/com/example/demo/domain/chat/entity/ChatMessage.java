package com.example.demo.domain.chat.entity;

import com.example.demo.domain.chat.dto.response.ChatMessageDto;
import com.example.demo.domain.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String userName;

    private String content;

    private MessageType type;

    @ManyToOne
    @JoinColumn(name = "chatRoom_id")
    private ChatRoom chatRoom;

    public ChatMessage(Long userId, String userName, String content, MessageType type, ChatRoom chatRoom) {
        this.userId = userId;
        this.userName = userName;
        this.content = content;
        this.type = type;
        this.chatRoom = chatRoom;
    }

    public ChatMessageDto of() {
        return new ChatMessageDto(
                this.chatRoom.getId(),
                this.userId,
                this.userName,
                this.content,
                this.getType(),
                this.getCreatedAt()
        );
    }

    public enum MessageType {
        ENTER, TALK, QUIT
    }
}
