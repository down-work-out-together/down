package com.example.demo.domain.fcm.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FCMTokenDto {

    @Schema(description = "FCM 토큰 ID", example = "1")
    private String id;

    @Schema(description = "FCM 토큰", example = "bk3RNwTe3H0:CI2k_HHwgIpoDKCIZvvDMExUdFQ3P1")
    private String accessToken;

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;
}
