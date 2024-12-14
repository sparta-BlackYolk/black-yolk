package com.sparta.blackyolk.delivery_service.application.dto.request;
public record DeliverySearchRequestDto(
        String name,
        String type,
        String address)
{
    public DeliverySearchRequestDto{};

    public DeliverySearchRequestDto(){
        this(null,null,null);
    };
}