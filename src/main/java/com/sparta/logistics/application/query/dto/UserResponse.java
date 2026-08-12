package com.sparta.logistics.application.query.dto;

import com.sparta.logistics.domain.entity.User;

import java.util.UUID;

public record UserResponse(
    UUID id,
    String name,
    String slackId
) {

  public static UserResponse from(User user) {
    return new UserResponse(
        user.getId(),
        user.getName(),
        user.getSlackId()
    );
  }

}
