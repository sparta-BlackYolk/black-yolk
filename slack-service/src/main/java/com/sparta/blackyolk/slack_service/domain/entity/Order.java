package com.sparta.blackyolk.slack_service.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Order {
    private String orderId;           // 주문 번호
    private String customerName;      // 주문자 이름
    private String customerEmail;     // 주문자 이메일
    private String productInfo;       // 상품 정보
    private String deliveryRequest;   // 배송 요청 사항
    private String origin;            // 발송지
    private String[] transitCenters;  // 경유지
    private String destination;       // 도착지
    private String deliveryContact;   // 배송 담당자
}