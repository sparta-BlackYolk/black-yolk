package com.sparta.msa_exam.order.order_service.presentation.api.controller;

import com.sparta.msa_exam.order.order_service.application.dto.request.OrderCreateRequestDto;
import com.sparta.msa_exam.order.order_service.application.dto.request.OrderSearchRequestDto;
import com.sparta.msa_exam.order.order_service.application.dto.request.OrderUpdateRequestDto;
import com.sparta.msa_exam.order.order_service.application.dto.response.OrderDetailResponseDto;
import com.sparta.msa_exam.order.order_service.application.dto.response.OrderListResponseDto;
import com.sparta.msa_exam.order.order_service.application.dto.response.OrderResponseDto;
import com.sparta.msa_exam.order.order_service.application.dto.response.PageResponseDto;
import com.sparta.msa_exam.order.order_service.domain.service.OrderService;
import com.sparta.msa_exam.order.order_service.presentation.api.controller.docs.OrderControllerDocs;
import com.sparta.msa_exam.order.order_service.presentation.api.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController extends OrderControllerDocs {

    private final OrderService orderService;

    @PostMapping("")
    @Override
    public Response<OrderResponseDto> createOrder(OrderCreateRequestDto requestDto) {
        return new Response<>(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(), orderService.createOrder(requestDto));
    }

    @DeleteMapping("/{orderId}")
    @Override
    public Response<OrderResponseDto> deleteOrder(@PathVariable UUID orderId) {
        return new Response<>(HttpStatus.NO_CONTENT.value(),HttpStatus.NO_CONTENT.getReasonPhrase(), orderService.deleteOrder(orderId));
    }

    @GetMapping("/{orderId}")
    @Override
    public Response<OrderDetailResponseDto> getOrderById(@PathVariable UUID orderId) {
        return new Response<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), orderService.getOrderById(orderId));
    }

    @PatchMapping("/{orderId}")
    @Override
    public Response<OrderResponseDto> updateOrder(@PathVariable UUID orderId, @RequestBody OrderUpdateRequestDto requestDto) {

        return new Response<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), orderService.updateOrder(orderId, requestDto));
    }

    @GetMapping
    @Override
    public Response<PageResponseDto<OrderListResponseDto>> getOrders(@ModelAttribute OrderSearchRequestDto requestDto) {
        return new Response<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), orderService.getOrders(requestDto));
    }
}