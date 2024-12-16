package com.sparta.blackyolk.auth_service.config;

import com.sparta.blackyolk.auth_service.user.entity.User;
import com.sparta.blackyolk.auth_service.user.entity.UserRoleEnum;
import com.sparta.blackyolk.auth_service.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class MasterAccountSetup {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner createMasterAccount() {
        return args -> {
            String username = "master";
            if (userRepository.findByUsername("master").isEmpty()) {
                User master = new User();
                master.setEmail("master@example.com");
                master.setUsername("master"); // name을 "master"로 설정
                master.setPassword(passwordEncoder.encode("master@123")); // 초기 비밀번호 설정
                master.setSlackId("masterSlack");
                master.setCreatedBy("master@example.com");
                master.setRole(UserRoleEnum.MASTER);
                userRepository.save(master);
            }
        };
    }
}
