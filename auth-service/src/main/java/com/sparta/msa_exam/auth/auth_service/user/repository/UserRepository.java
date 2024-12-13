package com.sparta.msa_exam.auth.auth_service.user.repository;

import com.sparta.msa_exam.auth.auth_service.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    // username으로 사용자 조회 (isDeleted가 false인 경우만)
    Optional<User> findByUsername(String username);

    // email로 사용자 조회 (isDeleted가 false인 경우만)
    Optional<User> findByEmail(String email);

    // isDeleted가 false인 사용자 목록을 페이징 처리하여 조회
    Page<User> findAllByIsDeletedFalse(Pageable pageable);

    // 특정 ID의 사용자 조회 (isDeleted가 false인 경우만)
    Optional<User> findByIdAndIsDeletedFalse(Long id);
}
