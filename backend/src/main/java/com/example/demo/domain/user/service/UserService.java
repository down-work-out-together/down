package com.example.demo.domain.user.service;

import com.example.demo.domain.user.entity.UserRoleEnumType;
import com.example.demo.domain.user.exception.EmailVerificationCodeMismatchException;
import com.example.demo.domain.user.exception.PendingEmailNotFoundException;
import com.example.demo.domain.user.mapper.EmailMapper;
import com.example.demo.domain.user.model.PendingEmail;
import com.example.demo.domain.user.model.User;
import com.example.demo.domain.user.model.User.UserId;
import com.example.demo.domain.user.mapper.UserMapper;
import com.example.demo.domain.user.repository.PendingEmailsRepository;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.auth.LoginEnumType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    @Value("${spring.mail.username}")
    private String FROM_EMAIL;

    private final UserRepository userRepository;
    private final PendingEmailsRepository pendingEmailsRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailMapper emailMapper;
    private final UserMapper userMapper;

    public UserId join(User user, String code) {

        // 이메일 확인 코드 보류 이메일에 있는지 확인
        PendingEmail pendingEmail = pendingEmailsRepository.findByEmail(user.getEmail())
                .map(emailMapper::entityToDomain)
                .orElseThrow(() -> new PendingEmailNotFoundException("이메일 유효성 검사를 진행하지 않은 이메일 주소에 대한 요청입니다."));

        // 이메일 확인 코드가 일치하는지 확인
        if (!pendingEmail.getAuthCode().equals(code)) {
            throw new EmailVerificationCodeMismatchException("이메일 인증코드가 일치하지 않습니다.");
        }

        // 회원 가입 처리
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setProvider(LoginEnumType.SERVICE);
        user.setRole(UserRoleEnumType.ROLE_USER);
        user.setProviderId(FROM_EMAIL);

        return Optional.of(user)
                .map(userMapper::domainToEntity)
                .map(userRepository::save)
                .map(userMapper::entityToDomain)
                .map(User::getId)
                .orElseThrow();
    }
}