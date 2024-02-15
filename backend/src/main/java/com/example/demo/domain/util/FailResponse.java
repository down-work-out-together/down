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
    @Schema(description = "응답 메시지", example = "요청이 실패했습니다.")
    private String message;

    public FailResponse(Data data) {
        this.success = false;
        this.data = data;
        this.message = "요청이 실패했습니다.";
    }

    public static FailResponse of(String data) {
        return new FailResponse(FailResponse.Data.of(data));
    }

    @lombok.Data
    @AllArgsConstructor(staticName = "of")
    public static class Data {
        @Schema(description = "에러 메시지", example = "상황에 맞는 에러 메시지.")
        private String errorMessage;
    }
}