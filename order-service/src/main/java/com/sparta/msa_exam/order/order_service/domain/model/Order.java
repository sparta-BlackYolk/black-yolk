package com.sparta.msa_exam.order.order_service.domain.model;

import com.sparta.msa_exam.order.order_service.application.dto.request.OrderCreateRequestDto;
import com.sparta.msa_exam.order.order_service.application.dto.request.OrderUpdateRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_order")
public class Order extends BaseEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    private UUID requestCompanyId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer totalPrice;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    public static Order create(OrderCreateRequestDto request) {
        return Order.builder()
                .requestCompanyId(request.requestCompanyId())
                .productId(request.productId())
                .quantity(request.quantity())
                .remarks(request.remarks())
                .build();
    }

    public void updateOrder(OrderUpdateRequestDto requestDto) {
        this.status = requestDto.orderStatus();
        this.quantity = requestDto.quantity();
        this.remarks = requestDto.remarks();
    }

    public void deleteOrder(UUID deletedBy) {
        super.delete(deletedBy);
    }
}