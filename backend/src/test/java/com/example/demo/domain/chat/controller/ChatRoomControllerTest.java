package com.example.demo.domain.chat.controller;

import com.example.demo.domain.chat.dto.request.ChatRoomCreateRequest;
import com.example.demo.domain.chat.dto.request.ChatRoomDeleteRequest;
import com.example.demo.domain.chat.dto.request.ChatRoomInviteRequest;
import com.example.demo.domain.chat.dto.response.*;
import com.example.demo.domain.chat.service.ChatRoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@WebMvcTest(ChatRoomController.class)
class ChatRoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatRoomService chatRoomService;

    ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("O 성공 회원의 모든 채팅방 조회")
    @Test
    @WithMockUser
    void getAllChatRoomByUserId() throws Exception {
        // Given
        List<SimpleChatRoomResponseDto> dtoList = new ArrayList<>();
        LongStream.rangeClosed(0,10).forEach(
                i -> dtoList.add(new SimpleChatRoomResponseDto("lastMessage", LocalDateTime.now(), "testChatRoom" + i, i)));
        ChatRoomListResponseDto result = new ChatRoomListResponseDto(dtoList);

        given(chatRoomService.getAllChatRoomByUserId(1L)).willReturn(result);

        // When
        mockMvc.perform(get("/api/v1/chatRoom/list/{userId}", 1L)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @DisplayName("O 성공 채팅방 정보 상세 조회")
    @Test
    @WithMockUser
    void getChatRoom() throws Exception {
        // Given
        ChatRoomDto dto = new ChatRoomDto();

        given(chatRoomService.getChatRoomWithUserListByChatRoomId(1L)).willReturn(dto);

        // When
        mockMvc.perform(get("/api/v1/chatRoom/{chatRoomId}", 1L)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @DisplayName("O 성공 채팅방 생성")
    @Test
    @WithMockUser
    void createChatRoom() throws Exception {
        // Given
        ChatRoomCreateRequest req = new ChatRoomCreateRequest(1L, List.of(2L,3L,4L), "testChatRoom");
        ChatRoomCreateResponseDto result = new ChatRoomCreateResponseDto();

        given(chatRoomService.createChatRoom(req)).willReturn(result);

        // When
        mockMvc.perform(post("/api/v1/chatRoom")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @DisplayName("O 성공 채팅방 퇴장")
    @Test
    @WithMockUser
    void exitChatRoom() throws Exception {
        // Given
        ChatRoomDeleteRequest req = new ChatRoomDeleteRequest(1L);
        ChatRoomDeleteResponseDto result = new ChatRoomDeleteResponseDto(1L);
        Long chatRoomId = 1L;

        given(chatRoomService.exitChatroom(chatRoomId, req)).willReturn(result);

        // When
        mockMvc.perform(patch("/api/v1/chatRoom/{chatRoomId}/exit", chatRoomId)
                        .param("userId", String.valueOf(req.getUserId()))
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @DisplayName("O 성공 채팅방 초대")
    @Test
    @WithMockUser
    void inviteChatRoom() throws Exception {
        // Given
        ChatRoomInviteRequest req = new ChatRoomInviteRequest(1L,2L, 1L);
        ChatRoomInviteResponseDto result = new ChatRoomInviteResponseDto();

        given(chatRoomService.inviteChatRoom(req)).willReturn(result);

        // When
        mockMvc.perform(post("/api/v1/chatRoom/invite")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }
}
