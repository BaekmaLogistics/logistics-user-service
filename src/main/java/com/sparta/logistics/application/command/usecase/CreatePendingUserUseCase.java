package com.sparta.logistics.application.command.usecase;

import com.sparta.logistics.application.command.dto.CreatePendingUserCommand;

public interface CreatePendingUserUseCase {

  void createPendingUser(CreatePendingUserCommand command);
}
