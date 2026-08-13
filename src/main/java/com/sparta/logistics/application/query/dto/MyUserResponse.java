package com.sparta.logistics.application.query.dto;

import com.sparta.logistics.domain.entity.User;
import com.sparta.logistics.domain.model.RequestedDeliveryType;
import com.sparta.logistics.domain.model.Role;

import java.util.UUID;

public record MyUserResponse(
    UUID userId,
    String name,
    String slackId,
    Role role,
    UUID hubId,
    UUID companyId,
    RequestedDeliveryType deliveryType
) {

  public static MyUserResponse from(User user) {
    return new MyUserResponse(
        user.getId(),
        user.getName(),
        user.getSlackId(),
        user.getRole(),
        user.getHubId(),
        user.getCompanyId(),
        user.getRequestedDeliveryType()
    );
  }
}
