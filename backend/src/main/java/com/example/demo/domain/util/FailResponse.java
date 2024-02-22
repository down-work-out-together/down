package com.example.demo.domain.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(description = "API 실패 응답")
public class FailResponse {

    @Schema(description = "성공 여부", example = "false")
    private boolean success;
    @Schema(description = "응답 데이터")
    private Data data;
    @Schema(description = "응답 메시지", example = "상황에 맞는 에러 메시지")
    private String message;

    public FailResponse(Data data, String message) {
        this.success = false;
        this.data = data;
        this.message = message;
    }

    public static FailResponse of(String data) {
        return new FailResponse(FailResponse.Data.of(data), data);
    }

    @lombok.Data
    @AllArgsConstructor(staticName = "of")
    public static class Data {
        // 수정 부분 RequestBody 들어갈 부분
        private String errorMessage;
    }
}