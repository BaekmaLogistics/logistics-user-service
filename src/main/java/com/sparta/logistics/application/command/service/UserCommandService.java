package com.sparta.logistics.application.command.service;

import com.sparta.logistics.application.command.dto.ApproveUserCommand;
import com.sparta.logistics.application.command.dto.CreatePendingUserCommand;
import com.sparta.logistics.application.command.usecase.ApproveUserUseCase;
import com.sparta.logistics.application.command.usecase.CreatePendingUserUseCase;
import com.sparta.logistics.domain.entity.DeliveryManager;
import com.sparta.logistics.domain.entity.User;
import com.sparta.logistics.domain.model.DeliveryManagerType;
import com.sparta.logistics.domain.model.Role;
import com.sparta.logistics.domain.repository.DeliveryManagerRepository;
import com.sparta.logistics.domain.repository.UserRepository;
import com.sparta.logistics.common.code.ErrorResponseCode;
import com.sparta.logistics.common.exception.ApiException;
import com.sparta.logistics.infrastructure.feign.client.auth.AuthServiceClient;
import com.sparta.logistics.infrastructure.feign.dto.ActivateAuthAccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandService implements
    CreatePendingUserUseCase,
    ApproveUserUseCase

{

  private final UserRepository userRepository;
  private final DeliveryManagerRepository deliveryManagerRepository;

  private final AuthServiceClient authServiceClient;

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


  @Override
  public void approveUser(ApproveUserCommand command) {

    User user = userRepository.findById(command.userId())
        .orElseThrow(() ->
            new ApiException(ErrorResponseCode.USER_NOT_FOUND));

    // PENDING 상태 확인 및 Role변경
    user.approve();

    // 배송 관리자의 경우 DeliveryManager 생성
    if (user.getRole() == Role.DELIVERY_MANAGER) {
      createDeliveryManager(user);
    }

    authServiceClient.activateAccount(
        user.getId(),
        new ActivateAuthAccountRequest(user.getRole().name())
    );
  }

  // DeliveryManager 생성
  private void createDeliveryManager(User user) {
    if (deliveryManagerRepository.existsByUserId(user.getId())) {
      return;
    }

    // 신청 타입
    DeliveryManagerType deliveryManagerType = DeliveryManagerType.from(
        user.getRequestedDeliveryType()
    );

    UUID hubId = user.getHubId();

    // order 계산
    int deliveryOrder = calculateNextDeliveryOrder(deliveryManagerType, hubId);

    DeliveryManager deliveryManager = DeliveryManager.create(
        user.getId(),
        hubId,
        deliveryManagerType,
        deliveryOrder
    );

    deliveryManagerRepository.save(deliveryManager);
  }

  // 순번 계산
  private int calculateNextDeliveryOrder(
      DeliveryManagerType deliveryManagerType,
      UUID hubId
  ) {

    // 배송 타입별로 순번계산
    int maxOrder = switch (deliveryManagerType) {
      case HUB_DELIVERY ->
          deliveryManagerRepository.findMaxHubDeliveryOrder();

      case COMPANY_DELIVERY ->
          deliveryManagerRepository.findMaxCompanyDelivery(hubId);
    };

    return maxOrder + 1;
  }
}
