package com.example.demo.domain.user.controller;

import com.example.demo.domain.user.dto.command.CheckEmailVerificationCommand;
import com.example.demo.domain.user.dto.command.UserJoinCommand;
import com.example.demo.domain.user.dto.response.CheckEmailVerificationResponseDTO;
import com.example.demo.domain.user.dto.response.SendEmailVerificationResponseDTO;
import com.example.demo.domain.user.dto.response.UserJoinResponseDTO;
import com.example.demo.domain.user.model.EmailVerification;
import com.example.demo.domain.user.model.PendingEmail;
import com.example.demo.domain.user.dto.command.CheckEmailCommand;
import com.example.demo.domain.user.dto.command.SendEmailVerificationCommand;
import com.example.demo.domain.user.dto.response.CheckEmailResponseDTO;
import com.example.demo.domain.user.model.User;
import com.example.demo.domain.user.service.EmailService;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.domain.util.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "회원가입",
        description = "회원가입을 위한 API"
)
@Slf4j
@RestController
@RequestMapping("${api.version-path}/user/join")
@RequiredArgsConstructor
public class UserJoinController {

    private final EmailService emailService;
    private final UserService userService;
    private final HttpSession httpSession;

    @Operation(
            summary = "이메일 중복 체크",
            description = "회원가입을 진행하는 사용자의 이메일이 이미 사용중인지 확인합니다.",
            tags = {"회원가입"}
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "중복 체크할 이메일 DTO",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CheckEmailCommand.class)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "이메일 중복 체크 성공"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "사용중이거나, 회원가입을 진행중인 이메일 입니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PostMapping("/check-email")
    public ResponseEntity<BaseResponse<CheckEmailResponseDTO>> checkEmail(
            @Validated @RequestBody CheckEmailCommand cmd
    ) {
        PendingEmail pendingEmail = emailService.registerPendingEmail(cmd.toPendingEmailDomain());

        CheckEmailResponseDTO responseDTO = CheckEmailResponseDTO.builder()
                .checkedEmail(pendingEmail.getEmail())
                .available(true)
                .build();

        SuccessResponse<CheckEmailResponseDTO> response =
                SuccessResponse.<CheckEmailResponseDTO>builder()
                        .data(responseDTO)
                        .message("사용 가능한 이메일입니다.")
                        .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "이메일 인증코드 발송",
            description = "회원가입을 진행하는 사용자의 이메일로 인증코드를 발송합니다.",
            tags = {"회원가입"}
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "인증코드 발송할 이메일 DTO",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SendEmailVerificationCommand.class)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "이메일 인증코드 발송 성공"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "사용중인 이메일입니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용가능한 이메일 확인을 먼저 진행해주시기 바랍니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "인증 횟수 5번을 초과하였습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PostMapping("/send-email-verification-code")
    public ResponseEntity<BaseResponse<SendEmailVerificationResponseDTO>> sendEmailVerification(
            @Validated @RequestBody SendEmailVerificationCommand cmd
    ) throws MessagingException {
        EmailVerification domain = emailService.sendEmailVerification(cmd.toEmailVerificationDomain());
        httpSession.setAttribute(domain.getEmail(), domain.getCode());

        SendEmailVerificationResponseDTO responseDTO = SendEmailVerificationResponseDTO.builder()
                .success(true)
                .email(domain.getEmail())
                .build();

        SuccessResponse<SendEmailVerificationResponseDTO> response =
                SuccessResponse.<SendEmailVerificationResponseDTO>builder()
                        .data(responseDTO)
                        .message("이메일 인증코드가 발송되었습니다.")
                        .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "이메일 인증코드 확인",
            description = "회원가입을 진행하는 사용자의 이메일로 발송된 인증코드를 확인합니다.",
            tags = {"회원가입"}
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "인증코드 확인할 이메일 DTO",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CheckEmailVerificationCommand.class)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "이메일 인증코드 확인 성공"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "이메일 유효성 검사 또는 이메일 인증코드 발송을 먼저 진행해주시기 바랍니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PostMapping("/check-email-verification-code")
    public ResponseEntity<BaseResponse<CheckEmailVerificationResponseDTO>> checkEmailVerification(
            @Validated @RequestBody CheckEmailVerificationCommand cmd
    ) {
        boolean success = emailService.checkEmailVerification(cmd.toEmailVerificationDomain());

        CheckEmailVerificationResponseDTO responseDTO = CheckEmailVerificationResponseDTO.builder()
                .email(cmd.getEmail())
                .result(success)
                .build();

        SuccessResponse<CheckEmailVerificationResponseDTO> response =
                SuccessResponse.<CheckEmailVerificationResponseDTO>builder()
                        .data(responseDTO)
                        .message("이메일 인증코드 확인이 완료되었습니다.")
                        .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "회원가입",
            description = "회원가입을 진행합니다.",
            tags = {"회원가입"}
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "회원가입 정보 DTO",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserJoinCommand.class)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "회원가입 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "이메일 인증코드가 일치하지 않습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "이메일 유효성 검사 또는 이메일 인증코드 발송을 먼저 진행해주시기 바랍니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<BaseResponse<UserJoinResponseDTO>> join(
            @Validated @RequestBody UserJoinCommand cmd
    ) {
        User.UserId userId = userService.join(cmd.toUserDomain(), cmd.getCode());

        UserJoinResponseDTO responseDTO = UserJoinResponseDTO.builder()
                .id(userId.getId())
                .email(cmd.getEmail())
                .build();

        SuccessResponse<UserJoinResponseDTO> response =
                SuccessResponse.<UserJoinResponseDTO>builder()
                        .data(responseDTO)
                        .message("회원가입이 완료되었습니다.")
                        .build();

        // 생성 코드 반환
        return ResponseEntity.ok(response);
    }
}