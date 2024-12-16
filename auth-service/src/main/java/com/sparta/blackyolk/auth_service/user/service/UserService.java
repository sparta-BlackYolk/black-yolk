package com.sparta.blackyolk.auth_service.user.service;

import com.sparta.blackyolk.auth_service.courier.entity.Courier;
import com.sparta.blackyolk.auth_service.courier.repository.CourierRepository;
import com.sparta.blackyolk.auth_service.jwt.JwtUtil;
import com.sparta.blackyolk.auth_service.user.dto.*;
import com.sparta.blackyolk.auth_service.user.entity.User;
import com.sparta.blackyolk.auth_service.user.entity.UserRoleEnum;
import com.sparta.blackyolk.auth_service.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CourierRepository courierRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 회원가입 처리
    public UserResponseDto signUp(UserSignUpRequestDto requestDto) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        User user = User.builder()
                .username(requestDto.getUsername())
                .email(requestDto.getEmail())
                .password(encodedPassword)
                .slackId(requestDto.getSlackId())
                .role(UserRoleEnum.VENDOR_MANAGER) // 기본 역할 설정
                .build();
        userRepository.save(user); // DB에 저장

        // Response DTO 생성 후 반환
        return UserResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .slackId(user.getSlackId())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }

    // 로그인 처리
    public LoginResponseDto login(LoginRequestDto requestDto, HttpServletResponse response) {
        // 사용자 조회
        User user = userRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        // JWT 생성
        String token = jwtUtil.createToken(user.getUsername(), user.getRole());
        response.setHeader(JwtUtil.AUTHORIZATION_HEADER, JwtUtil.BEARER_PREFIX + token); // 응답 헤더에 JWT 추가

        // 로그인 응답 DTO 반환
        return LoginResponseDto.builder()
                .userId(user.getId())
                .role(user.getRole()) // ENUM 타입 그대로 전달
                .build();
    }

    // 사용자 역할 변경
    public RoleUpdateResponseDto setRole(Long userId, UserRoleEnum role) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 역할 변경 및 저장
        user.setRole(role);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        // 응답 DTO 반환
        return RoleUpdateResponseDto.builder()
                .userId(user.getId())
                .role(user.getRole())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    // 모든 사용자 조회 (isDeleted가 false인 사용자만)
    public List<UserResponseDto> getAllUsers(Pageable pageable) {
        // 데이터베이스에서 isDeleted가 false인 사용자만 페이징 조회
        Page<User> usersPage = userRepository.findAllByIsDeletedFalse(pageable);

        // User 엔티티를 UserResponseDto로 변환하기 위해 DTO 리스트 생성
        List<UserResponseDto> userDtos = new ArrayList<>();
        for (User user : usersPage.getContent()) {
            UserResponseDto userDto = UserResponseDto.builder()
                    .userId(user.getId())           // 사용자 ID
                    .username(user.getUsername())   // 사용자 이름
                    .email(user.getEmail())         // 사용자 이메일
                    .slackId(user.getSlackId())     // 사용자 슬랙 ID
                    .role(user.getRole())           // 사용자 역할
                    .createdAt(user.getCreatedAt()) // 생성 일자
                    .build();
            userDtos.add(userDto);
        }
        return userDtos;
    }


    // 사용자 단일 조회 (isDeleted가 false인 사용자만)
    public UserResponseDto getUserByDynamicCondition(Long userId, String username, String email, String slackId) {
        User user = userRepository.findUserByDynamicCondition(userId, username, email, slackId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return UserResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .slackId(user.getSlackId())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }



    // 사용자 정보 수정
    public UserResponseDto updateUser(Long userId, UserSignUpRequestDto requestDto) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 정보 수정
        user.setUsername(requestDto.getUsername());
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setSlackId(requestDto.getSlackId());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        // 수정된 사용자 정보 반환
        return UserResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .slackId(user.getSlackId())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    // 사용자 삭제
    public DeleteResponseDto deleteUser(Long userId) {
        // 1. 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. userId: " + userId));

        // 2. 해당 사용자가 배송 담당자인 경우 Courier 소프트 삭제
        Courier courier = courierRepository.findByUserId(userId)
                .orElse(null);
        if (courier != null) {
            courier.setIsDeleted(true);
            courier.setDeletedAt(LocalDateTime.now());
            courier.setDeletedBy("System"); // 혹은 현재 로그인된 사용자 정보
            courierRepository.save(courier);
        }

        // 3. 사용자 소프트 삭제
        user.setIsDeleted(true);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);

        // 4. 삭제 응답 DTO 반환
        return DeleteResponseDto.builder()
                .userId(user.getId())
                .isDeleted(user.getIsDeleted())
                .deletedAt(user.getDeletedAt())
                .build();
    }

    public Optional<UserResponseDto> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .map(User::toDTO)
            .or(Optional::empty);
    }

    public Optional<UserResponseDto> getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(User::toDTO)
                .or(Optional::empty);

    }
}
