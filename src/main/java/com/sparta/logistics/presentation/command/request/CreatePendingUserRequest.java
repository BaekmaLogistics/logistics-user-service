package com.sparta.logistics.presentation.command.request;

import com.sparta.logistics.application.command.dto.CreatePendingUserCommand;
import com.sparta.logistics.domain.model.RequestedDeliveryType;
import com.sparta.logistics.domain.model.RequestedRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreatePendingUserRequest(

    @NotNull
    UUID userId,

    @NotBlank
    String name,

    @NotBlank
    String slackId,

    @NotNull
    RequestedRole requestedRole,

    RequestedDeliveryType requestedDeliveryType,

    UUID hubId,
    UUID companyId
) {

  public CreatePendingUserCommand toCommand() {
    return new CreatePendingUserCommand(
        userId,
        name,
        slackId,
        requestedRole,
        requestedDeliveryType,
        hubId,
        companyId
    );
  }
}
