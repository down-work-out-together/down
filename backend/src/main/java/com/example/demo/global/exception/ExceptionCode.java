package com.example.demo.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {
    LOG_NOT_FOUND("로그를 찾을 수 없습니다.", 400),

    // 이미 사용중인 사용자 이메일 입니다.
    ALREADY_EXIST_USER_EMAIL("이미 사용중인 사용자 이메일 입니다.", 400),

    // 이메일 인증 횟수를 5회 초과하였습니다.
    EXCEED_EMAIL_VERIFICATION_ATTEMPTS("이메일 인증 횟수를 5회 초과하였습니다.", 400),

    // 이메일 인증 코드가 존재하지 않습니다.
    NOT_EXIST_EMAIL_VERIFICATION_CODE("이메일 인증 코드가 존재하지 않습니다.", 400),

    // 보류 이메일이 존재하지 않습니다.
    NOT_EXIST_PENDING_EMAIL("보류 이메일이 존재하지 않습니다.", 400),

    // 이메일 인증 코드가 일치하지 않습니다.
    NOT_MATCH_EMAIL_VERIFICATION_CODE("이메일 인증 코드가 일치하지 않습니다.", 400),

    // 회원이 존재하지 않습니다.
    NOT_EXIST_USER("회원이 존재하지 않습니다.", 400),

    // 이메일 유효성 검사를 진행하지 않은 이메일 주소에 대한 요청입니다.
    NOT_VERIFIED_EMAIL("이메일 유효성 검사를 진행하지 않은 이메일 주소에 대한 요청입니다.", 400),

    // 이메일 인증코드가 일치하지 않습니다.
    NOT_MATCH_EMAIL_AUTH_CODE("이메일 인증코드가 일치하지 않습니다.", 400),

    // 비밀번호가 일치하지 않습니다.
    NOT_MATCH_PASSWORD("비밀번호가 일치하지 않습니다.", 400),

    // 카카오 OAuth 예외
    KAKAO_OAUTH_ERROR("카카오 OAuth 예외", 400)

    ;

    private final String message;
    private final int httpStatusCode;
}