package com.example.demo.domain.chat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chatRoomName;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatMessage> messageList;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatRoomUser> chatRoomUserList = new ArrayList<>();

    public ChatRoom(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }
}
