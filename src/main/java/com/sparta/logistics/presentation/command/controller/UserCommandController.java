package com.sparta.logistics.presentation.command.controller;

import com.sparta.logistics.application.command.dto.ApproveUserCommand;
import com.sparta.logistics.application.command.dto.RejectUserCommand;
import com.sparta.logistics.application.command.usecase.ApproveUserUseCase;
import com.sparta.logistics.application.command.usecase.RejectUserUseCase;
import com.sparta.logistics.common.code.GeneralResponseCode;
import com.sparta.logistics.domain.model.Role;
import com.sparta.logistics.infrastructure.security.GatewayUserPrincipal;
import com.sparta.logistics.presentation.command.request.RejectUserRequest;
import com.sparta.logistics.presentation.common.constant.HeaderConstants;
import com.sparta.logistics.presentation.common.dto.response.GeneralResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserCommandController {

  private final ApproveUserUseCase approveUserUseCase;
  private final RejectUserUseCase rejectUserUseCase;


  @PatchMapping("/{userId}/approval")
  @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
  public ResponseEntity<GeneralResponse<Void>> approve(
      @PathVariable UUID userId,
      @AuthenticationPrincipal GatewayUserPrincipal principal
  ) {

    ApproveUserCommand command = new ApproveUserCommand(
        userId,
        principal.userId(),
        principal.role()
    );

    approveUserUseCase.approveUser(command);

    return GeneralResponse.<Void>toResponseEntity(
        GeneralResponseCode.OK,
        null
    );
  }

  @PatchMapping("/{userId}/reject")
  @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
  public ResponseEntity<GeneralResponse<Void>> reject(
      @PathVariable UUID userId,
      @AuthenticationPrincipal GatewayUserPrincipal principal,
      @Valid @RequestBody RejectUserRequest request
      ) {

    rejectUserUseCase.rejectUser(
        request.toCommand(
            userId,
            principal.userId(),
            principal.role()
        )
    );

    return GeneralResponse.toResponseEntity(
        GeneralResponseCode.OK,
        null
    );
  }
}
