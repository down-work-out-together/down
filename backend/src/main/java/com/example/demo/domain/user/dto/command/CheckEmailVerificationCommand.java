package com.example.demo.domain.user.dto.command;

import com.example.demo.domain.user.model.EmailVerification;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "이메일 인증코드 확인 요청")
public class CheckEmailVerificationCommand {

    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Schema(description = "이메일", example = "test@gmail.com")
    private String email;

    @NotBlank(message = "인증코드는 필수 입력 값입니다.")
    @Size(min = 6, max = 6, message = "인증코드는 6자리입니다.")
    @Schema(description = "인증코드", example = "1A3B5C")
    private String code;
}
