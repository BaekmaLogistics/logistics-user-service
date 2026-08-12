package com.sparta.logistics.presentation.command.request;

import com.sparta.logistics.application.command.dto.RejectUserCommand;
import com.sparta.logistics.domain.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record RejectUserRequest(

    @NotBlank(message = "거절 사유는 필수입니다.")
    @Size(max = 255)
    String reason

) {

  public RejectUserCommand toCommand(
      UUID userId,
      UUID reviewerId,
      Role reviewerRole
  ) {

    return new RejectUserCommand(
        userId,
        reviewerId,
        reviewerRole,
        reason
    );

  }

}
