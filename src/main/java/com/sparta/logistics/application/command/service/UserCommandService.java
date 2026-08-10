package com.sparta.logistics.application.command.service;

import com.sparta.logistics.application.command.dto.CreatePendingUserCommand;
import com.sparta.logistics.application.command.usecase.CreatePendingUserUseCase;
import com.sparta.logistics.domain.entity.User;
import com.sparta.logistics.domain.repository.UserRepository;
import com.sparta.logistics.common.code.ErrorResponseCode;
import com.sparta.logistics.common.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandService implements CreatePendingUserUseCase {

  private final UserRepository userRepository;

  @Override
  public void createPendingUser(CreatePendingUserCommand command) {


    // UserId가 존재하는지 확인
    if (userRepository.existsById(command.userId())) {
      throw new ApiException(ErrorResponseCode.DUPLICATE_USER);
    }

    User user = User.createPending(
        command.userId(),
        command.name(),
        command.slackId(),
        command.requestedRole(),
        command.requestedDeliveryType(),
        command.hubId(),
        command.companyId()
    );

    userRepository.save(user);
  }
}
