package com.example.demo.global.event.chat;

import com.example.demo.domain.fcm.dto.request.FCMMessageDto;
import com.example.demo.domain.fcm.service.FCMService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class PrivateChatMessageCreatedListener {

    private final FCMService fcmService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleAlarm(PrivateChatMessageCreatedEvent event) {

        FCMMessageDto.Data data = FCMMessageDto.Data.builder()
                .receiverId(event.getReceiverId())
                .senderId(event.getSenderId())
                .senderName(event.getSenderName())
                .chatRoomId(event.getChatRoomId())
                .chatRoomName(event.getChatRoomName())
                .content(event.getContent())
                .build();

        fcmService.sendMessage(data);
    }


}
