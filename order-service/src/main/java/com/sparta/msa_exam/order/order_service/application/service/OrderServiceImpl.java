package com.sparta.msa_exam.order.order_service.application.service;

import com.sparta.msa_exam.order.order_service.application.dto.request.OrderCreateRequestDto;
import com.sparta.msa_exam.order.order_service.application.dto.request.OrderSearchRequestDto;
import com.sparta.msa_exam.order.order_service.application.dto.request.OrderUpdateRequestDto;
import com.sparta.msa_exam.order.order_service.application.dto.response.*;
import com.sparta.msa_exam.order.order_service.domain.client.CompanyClient;
import com.sparta.msa_exam.order.order_service.domain.client.DeliveryClient;
import com.sparta.msa_exam.order.order_service.domain.client.ProductClient;
import com.sparta.msa_exam.order.order_service.domain.exception.Error;
import com.sparta.msa_exam.order.order_service.domain.exception.OrderException;
import com.sparta.msa_exam.order.order_service.domain.model.Order;
import com.sparta.msa_exam.order.order_service.domain.model.OrderStatus;
import com.sparta.msa_exam.order.order_service.domain.repository.OrderRepository;
import com.sparta.msa_exam.order.order_service.domain.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CompanyClient companyClient;
    private final ProductClient productClient;
    private final DeliveryClient deliveryClient;

    //임시 변수 생성
    UUID deliveryId = UUID.randomUUID();

    @Override
    public OrderResponseDto createOrder(OrderCreateRequestDto request) {

        ProductResponseDto product = productClient.checkAmount(request.productId(), request.quantity());

        Order order = orderRepository.save(
                Order.builder()
                        .productId(product.productId())
                        .status(OrderStatus.RECEIVED)
                        .totalPrice(request.price()* request.quantity())
                        .remarks(request.remarks())
                        .quantity(request.quantity())
                        .price(request.price())
                        .requestCompanyId(request.requestCompanyId())
                        .build()
        );

        //TODO : 배송담당자 조회 (Feign Client)

        //TODO : create order 이벤트 발생

        return OrderResponseDto.builder()
                .deliveryId(deliveryId)
                .orderId(order.getId()).build();

    }

    @Override
    @Transactional
    public OrderResponseDto deleteOrder(UUID orderId) {
        Order order = orderRepository.findByIdAndIsDeleteFalse(orderId).orElseThrow(
                () -> new OrderException(Error.NOT_FOUND_ORDER)
        );

        //TODO : userID 로 바꾸기
        order.deleteOrder(orderId);

        UUID deliveryId = deliveryClient.deleteDeliveryByOrderId(orderId);


        return OrderResponseDto.builder()
                .orderId(order.getId())
                .deliveryId(deliveryId)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public OrderDetailResponseDto getOrderById(UUID orderId) {
        Order order = orderRepository.findByIdAndIsDeleteFalse(orderId).orElseThrow(
                () -> new OrderException(Error.NOT_FOUND_ORDER)
        );

        CompanyResponseDto recipientCompany = companyClient.findCompanyById(order.getRequestCompanyId());
        ProductInfoResponseDto product = productClient.findProductById(order.getProductId());
        CompanyResponseDto requestCompany = companyClient.findCompanyById(product.supplyCompanyId());
        UUID deliveryId = deliveryClient.getDeliveryByOrderId(orderId);

        return OrderDetailResponseDto.from(order, recipientCompany,requestCompany ,product, deliveryId);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponseDto<OrderListResponseDto> getOrders(OrderSearchRequestDto requestDto) {
        Page<Order> orders = findOrders(requestDto);

        List<UUID> requestCompanyIds = orders.map(Order::getRequestCompanyId).stream().distinct().toList();
        List<CompanyResponseDto> requestCompanies = companyClient.findCompaniesByIds(requestCompanyIds);

        List<UUID> productIds = orders.map(Order::getProductId).stream().distinct().toList();
        List<ProductResponseDto> products = productClient.findProductsByIds(productIds);

        Map<UUID, CompanyResponseDto> requestCompanyMap = requestCompanies.stream().collect(Collectors.toMap(CompanyResponseDto::companyId, c -> c));
        Map<UUID, ProductResponseDto> productMap = products.stream().collect(Collectors.toMap(ProductResponseDto::productId, p -> p));

        Page<OrderListResponseDto> results = orders.map(order -> {
            CompanyResponseDto requestCompany = requestCompanyMap.get(order.getRequestCompanyId());
            ProductResponseDto product = productMap.get(order.getProductId());
            return OrderListResponseDto.from(order, requestCompany, product);
        });

        return PageResponseDto.of(results);
    }


    @Transactional
    @Override
    public OrderResponseDto updateOrder(UUID orderId, OrderUpdateRequestDto requestDto) {

        Order order = orderRepository.findByIdAndIsDeleteFalse(orderId).orElseThrow(
                () -> new OrderException(Error.NOT_FOUND_ORDER)
        );

        UUID deliveryId = deliveryClient.getDeliveryByOrderId(orderId);

        order.updateOrder(requestDto);

        return OrderResponseDto.builder()
                .orderId(order.getId())
                .deliveryId(deliveryId)
                .build();
    }

    private Page<Order> findOrders(OrderSearchRequestDto requestDto) {
        if ("RECIPIENT_NAME".equals(requestDto.searchType())) {

            List<CompanyResponseDto> findCompanies = companyClient.findCompaniesByName(requestDto.searchValue());
            List<UUID> companyIds = findCompanies.stream().map(CompanyResponseDto::companyId).toList();

            return orderRepository.searchOrdersByCompanyIds(requestDto, companyIds);
        }
        return orderRepository.searchOrders(requestDto);
    }
}