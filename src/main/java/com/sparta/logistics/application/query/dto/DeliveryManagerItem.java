package com.sparta.logistics.application.query.dto;

import com.sparta.logistics.domain.model.DeliveryManagerType;

import java.util.UUID;

public record DeliveryManagerItem(
    UUID userId,
    String name,
    String slackId,
    DeliveryManagerType deliveryType,
    UUID hubId,
    Integer deliveryOrder
) {
}
