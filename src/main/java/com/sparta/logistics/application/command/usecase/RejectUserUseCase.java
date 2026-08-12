package com.sparta.logistics.application.command.usecase;

import com.sparta.logistics.application.command.dto.RejectUserCommand;

public interface RejectUserUseCase {

  void rejectUser(RejectUserCommand command);
}
