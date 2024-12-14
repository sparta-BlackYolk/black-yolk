package com.sparta.blackyolk.auth_service.courier.repository;

import com.sparta.blackyolk.auth_service.courier.entity.Courier;
import com.sparta.blackyolk.auth_service.user.entity.UserRoleEnum;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourierRepository extends JpaRepository<Courier, UUID> {
    Optional<Courier> findByCourierId(UUID courierId);
    Optional<Courier> findByUserId(Long userId);
    // 특정 허브의 배송 담당자 조회
    @Query("SELECT c FROM Courier c WHERE c.hubId = :hubId AND c.isDeleted = false")
    List<Courier> findAllByHubId(@Param("hubId") UUID hubId);

    @Query("SELECT MAX(CAST(c.deliveryNum AS integer)) FROM Courier c WHERE c.isDeleted = false")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Integer> findMaxDeliveryNumWithLock();

    boolean existsByUserId(Long userId);
}
