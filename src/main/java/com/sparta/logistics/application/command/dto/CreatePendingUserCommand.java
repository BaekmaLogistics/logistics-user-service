package com.sparta.logistics.application.command.dto;

import com.sparta.logistics.domain.model.RequestedDeliveryType;
import com.sparta.logistics.domain.model.RequestedRole;

import java.util.UUID;

public record CreatePendingUserCommand(
    UUID userId,
    String name,
    String slackId,
    RequestedRole requestedRole,
    RequestedDeliveryType requestedDeliveryType,
    UUID hubId,
    UUID companyId
) {
}
