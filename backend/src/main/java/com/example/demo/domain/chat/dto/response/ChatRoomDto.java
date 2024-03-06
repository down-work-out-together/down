package com.example.demo.domain.chat.dto.response;

import com.example.demo.domain.user.dto.response.UserInfoResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "채팅방 정보 응답")
public class ChatRoomDto {

    @Schema(description = "채팅방 ID", example = "1")
    private Long chatRoomId;

    @Schema(description = "채팅방 이름", example = "채팅방1")
    private String chatRoomName;

    @Schema(description = "회원 정보 목록")
    private List<UserInfoResponseDTO> userInfoResponseDTOList;
}
