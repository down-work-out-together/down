package com.example.demo.domain.chat.controller;

import com.example.demo.domain.chat.dto.request.ChatRoomCreateRequest;
import com.example.demo.domain.chat.dto.request.ChatRoomDeleteRequest;
import com.example.demo.domain.chat.dto.response.*;
import com.example.demo.domain.chat.service.ChatRoomService;
import com.example.demo.domain.util.BaseResponse;
import com.example.demo.domain.util.FailResponse;
import com.example.demo.domain.util.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(
        name = "채팅방",
        description = "채팅방을 위한 API"
)
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @Operation(
            summary = "채팅방 목록 조회",
            description = "채팅방 목록을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "채팅방 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SimpleChatRoomResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description =
                            """
                            - 회원이 존재하지 않습니다.
                            """,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @GetMapping("/api/v1/chatRoom/list")
    public ResponseEntity<BaseResponse<ChatRoomListResponseDto>> getChatRoomList(@Parameter(description = "사용자 ID", example = "1")
                                                                             @RequestParam long userId) {

        ChatRoomListResponseDto result = chatRoomService.getAllChatRoomByUserId(userId);

        SuccessResponse<ChatRoomListResponseDto> response =  SuccessResponse.<ChatRoomListResponseDto>builder()
                .data(result)
                .message("채팅방 목록 조회 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "채팅방 정보 조회",
            description = "채팅방 정보를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "채팅방 정보 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChatRoomDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description =
                            """
                            - 채팅방이 존재하지 않습니다.
                            """,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @GetMapping("/api/v1/chatRoom/{chatRoomId}")
    public ResponseEntity<BaseResponse<ChatRoomDto>> getChatRoom(@Parameter(description = "채팅방 ID", example = "1")
                                                                     @PathVariable long chatRoomId) {
        ChatRoomDto result = chatRoomService.getChatRoomWithUserListByChatRoomId(chatRoomId);

        SuccessResponse<ChatRoomDto> response =  SuccessResponse.<ChatRoomDto>builder()
                .data(result)
                .message("채팅방 정보 조회 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "채팅방 생성",
            description = "채팅방을 생성합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "채팅방 생성 요청",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = ChatRoomCreateRequest.class)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "채팅방 생성 요청 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description =
                            """
                            - 회원이 존재하지 않습니다.
                            """,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PostMapping("/api/v1/chatRoom")
    public ResponseEntity<BaseResponse<ChatRoomCreateResponseDto>> createChatRoom(@Valid @RequestBody ChatRoomCreateRequest req) {

        ChatRoomCreateResponseDto result = chatRoomService.createChatRoom(req);

        SuccessResponse<ChatRoomCreateResponseDto> response =  SuccessResponse.<ChatRoomCreateResponseDto>builder()
                .data(result)
                .message("채팅방 생성 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "채팅방 퇴장",
            description = "채팅방에서 퇴장합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "채팅방 퇴장 요청",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = ChatRoomDeleteRequest.class)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "채팅방 퇴장 성공"
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
    @DeleteMapping("/api/v1/chatRoom/{chatRoomId}")
    public ResponseEntity<BaseResponse<ChatRoomDeleteResponseDto>> exitChatRoom(@Valid @RequestBody ChatRoomDeleteRequest req) {

        ChatRoomDeleteResponseDto result = chatRoomService.exitChatroom(req);

        SuccessResponse<ChatRoomDeleteResponseDto> response =  SuccessResponse.<ChatRoomDeleteResponseDto>builder()
                .data(result)
                .message("채팅방 삭제 성공")
                .build();

        return ResponseEntity.ok(response);

    }

}
