package com.example.demo.domain.chat.controller;


import com.example.demo.domain.chat.dto.request.ChatMessageCreateRequest;
import com.example.demo.domain.chat.dto.request.ChatMessageReadCondition;
import com.example.demo.domain.chat.dto.response.ChatMessageReadResponseDto;
import com.example.demo.domain.chat.service.ChatMessageService;
import com.example.demo.domain.util.BaseResponse;
import com.example.demo.domain.util.FailResponse;
import com.example.demo.domain.util.SuccessResponse;
import com.example.demo.global.auth.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(
        name = "그룹 채팅 메시지",
        description = "그룹 채팅 메시지를 위한 API"
)
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * websocket "/pub/group/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/group/chat/message")
    public void message(ChatMessageCreateRequest req, @RequestHeader("Authorization") String authorizationHeader) {
        jwtTokenProvider.validateToken(jwtTokenProvider.authorizationToJwt(authorizationHeader));
        chatMessageService.saveChatMessage(req);
    }

    /**
     * 특정 채팅방의 최근 메시지 20개를 가져옴
     * */
    @Operation(
            summary = "채팅방 메시지 조회",
            description = "채팅방의 최근 메시지를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "채팅방 최근 메시지 조회 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description =
                            """
                            - 회원이 존재하지 않습니다.
                            - 채팅방이 존재하지 않습니다.
                            - 채팅방에 초대되지 않은 회원입니다.
                            """,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @GetMapping("/api/v1/group/chat/message")
    public ResponseEntity<BaseResponse<ChatMessageReadResponseDto>> messageList(@Valid @ModelAttribute ChatMessageReadCondition cond){
        ChatMessageReadResponseDto result = chatMessageService.findLatestMessage(cond);

        SuccessResponse<ChatMessageReadResponseDto> response = SuccessResponse.<ChatMessageReadResponseDto>builder()
                .data(result)
                .message("채팅 메시지 목록 조회 성공")
                .build();

        return ResponseEntity.ok(response);
    }
}
