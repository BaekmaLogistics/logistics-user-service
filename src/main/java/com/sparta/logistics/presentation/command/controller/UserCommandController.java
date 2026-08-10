package com.sparta.logistics.presentation.command.controller;

import com.sparta.logistics.application.command.dto.ApproveUserCommand;
import com.sparta.logistics.application.command.usecase.ApproveUserUseCase;
import com.sparta.logistics.common.code.GeneralResponseCode;
import com.sparta.logistics.domain.model.Role;
import com.sparta.logistics.presentation.common.constant.HeaderConstants;
import com.sparta.logistics.presentation.common.dto.response.GeneralResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserCommandController {

  private final ApproveUserUseCase approveUserUseCase;

  @PatchMapping("/{userId}/approval")
  public ResponseEntity<GeneralResponse<Void>> approve(
      @PathVariable UUID userId,
      @RequestHeader(HeaderConstants.USER_ID) UUID reviewerId,
      @RequestHeader(HeaderConstants.USER_ROLE) Role reviewerRole
  ) {

    ApproveUserCommand command = new ApproveUserCommand(
        userId,
        reviewerId,
        reviewerRole
    );

    approveUserUseCase.approveUser(command);

    return GeneralResponse.<Void>toResponseEntity(
        GeneralResponseCode.OK,
        null
    );
  }
}
