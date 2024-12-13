package com.sparta.msa_exam.auth.auth_service.user.entity;

public enum UserRoleEnum {
    VENDOR_MANAGER("VENDOR_MANAGER"),
    HUB_DELIVERY("HUB_DELIVERY"),
    COMPANY_DELIVERY("COMPANY_DELIVERY"),
    HUB_ADMIN("HUB_ADMIN"),
    MASTER("MASTER");
    private final String role;

    UserRoleEnum(String role) {
        this.role = role;
    }

    public String getAuthority() {
        return this.role;
    }

    public static class Role {
        public static final String VENDOR_MANAGER = "VENDOR_MANAGER";
        public static final String HUB_DELIVERY = "HUB_DELIVERY";
        public static final String COMPANY_DELIVERY = "COMPANY_DELIVERY";
        public static final String HUB_ADMIN = "HUB_ADMIN";
        public static final String MASTER = "MASTER";
    }
}
