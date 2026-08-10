package com.sparta.logistics.application.command.usecase;

import com.sparta.logistics.application.command.dto.ApproveUserCommand;

import java.util.UUID;

public interface ApproveUserUseCase {

  void approveUser(ApproveUserCommand command);
}
