package com.sparta.logistics.application.command.service;

import com.sparta.logistics.application.command.dto.ApproveUserCommand;
import com.sparta.logistics.application.command.dto.CreatePendingUserCommand;
import com.sparta.logistics.application.command.dto.RejectUserCommand;
import com.sparta.logistics.application.command.usecase.ApproveUserUseCase;
import com.sparta.logistics.application.command.usecase.CreatePendingUserUseCase;
import com.sparta.logistics.application.command.usecase.RejectUserUseCase;
import com.sparta.logistics.domain.entity.DeliveryManager;
import com.sparta.logistics.domain.entity.User;
import com.sparta.logistics.domain.model.DeliveryManagerType;
import com.sparta.logistics.domain.model.RequestedDeliveryType;
import com.sparta.logistics.domain.model.RequestedRole;
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
    ApproveUserUseCase,
    RejectUserUseCase

{

  private final UserRepository userRepository;
  private final DeliveryManagerRepository deliveryManagerRepository;

  private final AuthServiceClient authServiceClient;


  private static final long MAX_DELIVERY_MANAGER_COUNT = 10L;


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

    User targetUser = userRepository.findByIdAndDeletedAtIsNull(command.userId())
        .orElseThrow(() ->
            new ApiException(ErrorResponseCode.USER_NOT_FOUND));

    // 승인 권한 검사
    validateApprovalAuthority(
        command.reviewerId(),
        command.reviewerRole(),
        targetUser
    );

    DeliveryManager deliveryManager = null;

    // 배송 관리자의 경우 DeliveryManager 생성
    if (targetUser.getRequestedRole() == RequestedRole.DELIVERY_MANAGER) {
      deliveryManager = createDeliveryManager(targetUser);
    }

    // PENDING 상태 확인 및 Role변경
    targetUser.approve(command.reviewerId());

    if (deliveryManager != null) {
      deliveryManagerRepository.save(deliveryManager);
    }


    authServiceClient.activateAccount(
        targetUser.getId(),
        new ActivateAuthAccountRequest(targetUser.getRole().name())
    );
  }

  // 회원가입 거절
  @Override
  public void rejectUser(RejectUserCommand command) {

    User targetUser = userRepository.findByIdAndDeletedAtIsNull(command.userId())
        .orElseThrow(() ->
            new ApiException(ErrorResponseCode.USER_NOT_FOUND));

    // 승인 권한 검사
    validateApprovalAuthority(
        command.reviewerId(),
        command.reviewerRole(),
        targetUser
    );

    targetUser.reject(
        command.reason(),
        command.reviewerId()
    );

    authServiceClient.rejectAccount(targetUser.getId());

  }


  // 승인 권한 검사
  private void validateApprovalAuthority(
      UUID reviewerId,
      Role reviewerRole,
      User targetUser
  ) {

    // 마스터 관리자는 모든 가입 신청 승인 가능
    if (reviewerRole == Role.MASTER) {
      return;
    }

    // 허브 관리자가 아니면 가입 신청 승인 불가
    if (reviewerRole != Role.HUB_MANAGER) {
      throw new ApiException(ErrorResponseCode.USER_APPROVAL_FORBIDDEN);
    }

    // 허브 배송 담당자는 특정 허브 소속이 아니므로 HUB_MANAGER가 승일할 수 없음
    if (targetUser.getRequestedRole()
        == RequestedRole.DELIVERY_MANAGER
        && targetUser.getRequestedDeliveryType()
        == RequestedDeliveryType.HUB_DELIVERY) {
      throw new ApiException(ErrorResponseCode.USER_APPROVAL_FORBIDDEN);
    }

    User reviewer = userRepository.findByIdAndDeletedAtIsNull(reviewerId)
        .orElseThrow(() ->
            new ApiException(ErrorResponseCode.USER_NOT_FOUND));

    UUID reviewerHubId = reviewer.getHubId();
    UUID targetHubId = targetUser.getHubId();

    // 허브 관리자가 승인할 권한이 있는지
    if (reviewer.getRole() != Role.HUB_MANAGER
        || reviewerHubId == null
        || targetHubId == null
        || !targetHubId.equals(reviewerHubId)
    ) {
      throw new ApiException(ErrorResponseCode.USER_APPROVAL_FORBIDDEN);
    }
  }

  // 배송 담당자 생성
  private DeliveryManager createDeliveryManager(User targetUser) {

    // 배송 담당자가 존재하는지
    if (deliveryManagerRepository.existsById(targetUser.getId())) {
      throw new ApiException(
          ErrorResponseCode.DUPLICATE_DELIVERY_MANAGER
      );
    }

    // 신청 타입
    DeliveryManagerType deliveryManagerType = DeliveryManagerType.from(
        targetUser.getRequestedDeliveryType()
    );

    UUID hubId = targetUser.getHubId();

    // 배송 담당자 타입별 배송 담당자 생성
    return switch (deliveryManagerType) {
      case HUB_DELIVERY -> createHubDeliveryManager(targetUser.getId());
      case COMPANY_DELIVERY -> createCompanyDeliveryManager(targetUser.getId(), hubId);
    };
  }

  // 허브 배송 담당자 생성
  private DeliveryManager createHubDeliveryManager(UUID id) {

    // 삭제를 제외한 인원
    long activeCount =
        deliveryManagerRepository
            .countByDeliveryManagerTypeAndDeletedAtIsNull(
                DeliveryManagerType.HUB_DELIVERY
            );

    // 배송 담당 인원 확인
    validateCapacity(activeCount);

    // 순번 계산
    int deliveryOrder = calculateNextDeliveryOrder(
        DeliveryManagerType.HUB_DELIVERY,
        null
    );

    // 생성
    return DeliveryManager.create(
        id,
        null,
        DeliveryManagerType.HUB_DELIVERY,
        deliveryOrder
    );
  }

  // 업체 배송 담당자 생성
  private DeliveryManager createCompanyDeliveryManager(UUID id, UUID hubId) {


    if (hubId == null) {
      throw new ApiException(
          ErrorResponseCode.DELIVERY_MANAGER_HUB_REQUIRED
      );
    }

    // 실제로 존재하는 hubId인지 확인해야 함


    long activeCount =
        deliveryManagerRepository
            .countByDeliveryManagerTypeAndHubIdAndDeletedAtIsNull(
                DeliveryManagerType.COMPANY_DELIVERY,
                hubId
            );

    // 배송 담당 인원 확인
    validateCapacity(activeCount);

    int deliveryOrder =
        calculateNextDeliveryOrder(
            DeliveryManagerType.COMPANY_DELIVERY,
            hubId
        );

    return DeliveryManager.create(
        id,
        hubId,
        DeliveryManagerType.COMPANY_DELIVERY,
        deliveryOrder
    );
  }


  private void validateCapacity(long activeCount) {
    if (activeCount >= MAX_DELIVERY_MANAGER_COUNT) {
      throw new ApiException(
          ErrorResponseCode.DELIVERY_MANAGER_LIMIT_EXCEEDED
      );
    }
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
