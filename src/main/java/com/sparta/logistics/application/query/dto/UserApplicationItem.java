package com.sparta.logistics.application.query.dto;

import com.sparta.logistics.domain.entity.User;
import com.sparta.logistics.domain.model.RequestedDeliveryType;
import com.sparta.logistics.domain.model.RequestedRole;

import java.time.Instant;
import java.util.UUID;

public record UserApplicationItem(
    UUID userId,
    String name,
    String slackId,
    RequestedRole requestedRole,
    RequestedDeliveryType requestedDeliveryType,
    UUID hubId,
    UUID companyId,
    Instant appliedAt
) {

  public static UserApplicationItem from(User user) {
    return new UserApplicationItem(
        user.getId(),
        user.getName(),
        user.getSlackId(),
        user.getRequestedRole(),
        user.getRequestedDeliveryType(),
        user.getHubId(),
        user.getCompanyId(),
        user.getCreatedAt()
    );
  }
}
