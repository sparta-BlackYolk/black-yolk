package com.sparta.blackyolk.auth_service.user.controller;

import com.sparta.blackyolk.auth_service.user.dto.*;
import com.sparta.blackyolk.auth_service.user.entity.User;
import com.sparta.blackyolk.auth_service.user.entity.UserRoleEnum;
import com.sparta.blackyolk.auth_service.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 1. 회원가입
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signUp(@Valid @RequestBody UserSignUpRequestDto requestDto) {
        UserResponseDto responseDto = userService.signUp(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 2. 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response) {
        LoginResponseDto responseDto = userService.login(requestDto, response);
        return ResponseEntity.ok(responseDto);
    }

    // 3. 권한 설정
    @PutMapping("/{userId}/role")
    public ResponseEntity<RoleUpdateResponseDto> setRole(@PathVariable Long userId, @RequestBody RoleRequestDto requestDto) {
        RoleUpdateResponseDto responseDto = userService.setRole(userId, requestDto.toEnum());
        return ResponseEntity.ok(responseDto);
    }

    // 4. 사용자 전체 조회 (페이징)
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        List<UserResponseDto> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    // 5. 사용자 정보 조회
    @GetMapping("/search")
    public ResponseEntity<UserResponseDto> getUserByDynamicCondition(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String slackId
    ) {
        UserResponseDto responseDto = userService.getUserByDynamicCondition(userId, username, email, slackId);
        return ResponseEntity.ok(responseDto);
    }

    // 6. 정보 수정
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long userId, @RequestBody UserSignUpRequestDto requestDto) {
        UserResponseDto responseDto = userService.updateUser(userId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 7. 사용자 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<DeleteResponseDto> deleteUser(@PathVariable Long userId) {
        DeleteResponseDto responseDto = userService.deleteUser(userId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
        UserResponseDto responseDto = userService.getUserById(userId);
        return ResponseEntity.ok(responseDto);
    }
}
