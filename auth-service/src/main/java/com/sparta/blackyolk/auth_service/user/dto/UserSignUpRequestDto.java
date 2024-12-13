package com.sparta.blackyolk.auth_service.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignUpRequestDto {
    @Size(min = 4, max = 10, message = "사용자 이름은 4자 이상 10자 이하이어야 합니다.")
    @Pattern(regexp = "^[a-z0-9]*$", message = "사용자 이름은 소문자와 숫자로만 구성되어야 합니다.")
    private String username;

    @Email(message = "이메일 형식이 맞지 않습니다.")
    private String email;

    @Size(min = 8, max = 15, message = "비밀번호는 8자 이상 15자 이하이어야 합니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$",
            message = "비밀번호는 최소 하나의 대문자, 하나의 소문자, 하나의 숫자 및 하나의 특수 문자를 포함해야 합니다.")
    private String password;

    private String slackId;


    private String phoneNumber;
}
