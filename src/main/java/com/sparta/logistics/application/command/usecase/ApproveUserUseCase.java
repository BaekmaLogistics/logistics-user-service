package com.sparta.logistics.application.command.usecase;

import com.sparta.logistics.application.command.dto.ApproveUserCommand;

public interface ApproveUserUseCase {

  void approveUser(ApproveUserCommand command);
}
