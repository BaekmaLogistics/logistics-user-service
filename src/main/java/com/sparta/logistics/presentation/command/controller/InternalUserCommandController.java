package com.sparta.logistics.presentation.command.controller;

import com.sparta.logistics.application.command.usecase.CreatePendingUserUseCase;
import com.sparta.logistics.presentation.command.request.CreatePendingUserRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/api/v1/users")
public class InternalUserCommandController {

  private final CreatePendingUserUseCase createPendingUserUseCase;

  @SecurityRequirements
  @PostMapping("/signup")
  public void createPendingUser(
      @Valid @RequestBody CreatePendingUserRequest request) {

    createPendingUserUseCase.createPendingUser(request.toCommand());
  }
}
