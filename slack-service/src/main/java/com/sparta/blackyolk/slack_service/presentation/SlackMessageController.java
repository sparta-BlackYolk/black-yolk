package com.sparta.blackyolk.slack_service.presentation;


import com.sparta.blackyolk.slack_service.application.service.GeminiService;
import com.sparta.blackyolk.slack_service.application.service.SlackMessageService;
import com.sparta.blackyolk.slack_service.domain.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/slack")
public class SlackMessageController {

    private final SlackMessageService slackMessageService;
    private final GeminiService geminiService;

    // 특정 사용자에게 메시지를 보내고 데이터베이스에 저장
    @PostMapping("/send")
    public String sendMessageToUser(@RequestParam String userId, @RequestParam String orderId) {

        // 주문 정보 가져오기 (실제로는 DB에서 주문 정보를 조회하는 로직 필요)
        Order order = getOrderDetails(orderId);  // 예시로 주문 정보 가져오는 메서드

        // 최종 발송 시한 계산
        String finalDeadline = geminiService.getFinalShippingDeadline(order);

        // 메시지 생성
        String message = buildMessage(order, finalDeadline);

        boolean success = slackMessageService.sendMessage(userId, message);

        if (success) {
            return "메시지가 성공적으로 전송되었습니다.";
        } else {
            return "메시지 전송에 실패했습니다.";
        }
    }
    // 주문 정보 예시로 가져오기 (실제 환경에서는 DB에서 조회해야 함)
    private Order getOrderDetails(String orderId) {
        // 예시로 주문 정보를 하드코딩 (DB에서 주문 정보를 조회하는 로직이 필요)
        return new Order("1", "김말숙", "msk@seafood.world", "마른 오징어 50박스",
                "12월 12일 오후 3시까지는 보내주세요!", "경기 북부 센터",
                new String[] {"대전광역시 센터", "부산광역시 센터"},
                "부산시 사하구 낙동대로 1번길 1 해산물월드", "고길동 / kdk@sparta.world");
    }

    // 메시지 내용 구성
    private String buildMessage(Order order, String finalDeadline) {
        return String.format("주문 번호: %s\n주문자 정보: %s / %s\n상품 정보: %s\n요청 사항: %s\n발송지: %s\n" +
                        "경유지: %s\n도착지: %s\n배송담당자: %s / %s\n\n위 내용을 기반으로 도출된 최종 발송 시한은 %s",
                order.getOrderId(),
                order.getCustomerName(), order.getCustomerEmail(),
                order.getProductInfo(),
                order.getDeliveryRequest(),
                order.getOrigin(),
                String.join(", ", order.getTransitCenters()),
                order.getDestination(),
                order.getDeliveryContact(), order.getDeliveryContact(),
                finalDeadline);
    }
}