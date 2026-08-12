package com.sparta.logistics.application.query.dto;

import com.sparta.logistics.domain.entity.User;

import java.util.UUID;

public record UserAccessResponse(
    UUID userId,
    String role,
    UUID hubId,
    UUID companyId
) {

  public static UserAccessResponse from(User user) {
    return new UserAccessResponse(
        user.getId(),
        user.getRole().name(),
        user.getHubId(),
        user.getCompanyId()
    );
  }
}
