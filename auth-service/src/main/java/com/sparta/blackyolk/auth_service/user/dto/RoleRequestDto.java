package com.sparta.blackyolk.auth_service.user.dto;

import com.sparta.blackyolk.auth_service.user.entity.UserRoleEnum;

public class RoleRequestDto {
    private String role;

    public UserRoleEnum toEnum() {
        try {
            return UserRoleEnum.valueOf(role);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    // Getter, Setter
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

