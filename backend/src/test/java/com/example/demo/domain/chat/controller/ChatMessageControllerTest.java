package com.example.demo.domain.chat.controller;

import com.example.demo.domain.chat.dto.request.ChatMessageReadCondition;
import com.example.demo.domain.chat.dto.response.ChatMessageDto;
import com.example.demo.domain.chat.dto.response.ChatMessageReadResponseDto;
import com.example.demo.domain.chat.entity.ChatMessage;
import com.example.demo.domain.chat.service.ChatMessageService;
import com.example.demo.global.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@WebMvcTest(ChatMessageController.class)
class ChatMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatMessageService chatMessageService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("O 성공 최근 메시지 조회")
    @Test
    @WithMockUser
    void getMessageList() throws Exception {
        // Given
        ChatMessageReadCondition cond = new ChatMessageReadCondition(1L, 0L, 20);
        ChatMessageReadResponseDto result = new ChatMessageReadResponseDto(10, false, List.of(
                ChatMessageDto.builder()
                        .chatRoomId(1L)
                        .userName("testUsername")
                        .userId(1L)
                        .type(ChatMessage.MessageType.TALK)
                        .createdAt(LocalDateTime.now())
                        .build()
        ));

        given(chatMessageService.findLatestMessage(cond)).willReturn(result);

        // When
        mockMvc.perform(get("/api/v1/group/chat/message")
                        .param("size", String.valueOf(cond.getSize()))
                        .param("chatRoomId", String.valueOf(cond.getChatRoomId()))
                        .param("lastMessageId", String.valueOf(cond.getLastChatMessageId()))
                        .with(csrf()))
                .andExpect(status().isOk());
    }
}
