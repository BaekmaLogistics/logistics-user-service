package com.sparta.logistics.application.query.service;

import com.sparta.logistics.application.query.dto.*;
import com.sparta.logistics.application.query.usecase.GetUserAccessUseCase;
import com.sparta.logistics.application.query.usecase.GetUserApplicationsUseCase;
import com.sparta.logistics.application.query.usecase.SearchUserUseCase;
import com.sparta.logistics.common.code.ErrorResponseCode;
import com.sparta.logistics.common.exception.ApiException;
import com.sparta.logistics.domain.entity.User;
import com.sparta.logistics.domain.model.ApprovalStatus;
import com.sparta.logistics.domain.model.RequestedDeliveryType;
import com.sparta.logistics.domain.model.RequestedRole;
import com.sparta.logistics.domain.model.Role;
import com.sparta.logistics.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserQueryService implements
    SearchUserUseCase,
    GetUserAccessUseCase,
    GetUserApplicationsUseCase
{

  private final UserRepository userRepository;


  @Override
  public List<UserResponse> searchUsers(SearchUserQuery query) {
    return userRepository.findAllByIdInAndApprovalStatusAndDeletedAtIsNull(
            query.userIds().stream().distinct().toList(),
            ApprovalStatus.APPROVED
        )
        .stream()
        .map(UserResponse::from)
        .toList();
  }

  @Override
  public UserAccessResponse getUserAccess(UUID userId) {

    // APPROVED, 삭제되지 않은 User
    User user = userRepository.findByIdAndApprovalStatusAndDeletedAtIsNull(
        userId,
        ApprovalStatus.APPROVED
    ).orElseThrow(() ->
        new ApiException(ErrorResponseCode.USER_NOT_FOUND)
    );

    return UserAccessResponse.from(user);
  }


  @Override
  public UserApplicationPageResponse getUserApplications(GetUserApplicationsQuery query) {
    Page<User> applications = switch (query.reviewerRole()) {
      case MASTER -> getApplicationsForMaster(query);
      case HUB_MANAGER -> getApplicationsForHubManager(query);

      default -> throw new ApiException(ErrorResponseCode.USER_APPLICATION_READ_FORBIDDEN);
    };


    return UserApplicationPageResponse.from(
        applications.map(UserApplicationItem::from)
    );
  }

  // 회원가입 목록 조회(관리자)
  private Page<User> getApplicationsForMaster(GetUserApplicationsQuery query) {
    return userRepository.findAllByApprovalStatusAndDeletedAtIsNull(
        ApprovalStatus.PENDING,
        query.pageable()
    );
  }

  // 회원가입 목록 조회(허브 관리자)
  private Page<User> getApplicationsForHubManager(GetUserApplicationsQuery query) {

    // HUB_MANAGER 유저 가져오기
    User hubManager = findApprovedHubManager(
        query.reviewerId()
    );

    return userRepository
        .findReadableApplicationsByHubManager(
            ApprovalStatus.PENDING,
            hubManager.getHubId(),
            RequestedRole.DELIVERY_MANAGER,
            RequestedDeliveryType.HUB_DELIVERY,
            query.pageable()
        );
  }

  // HUB_MANAGER 유저 가져오기
  private User findApprovedHubManager(UUID reviewerId) {

    User reviewer = userRepository
        .findByIdAndApprovalStatusAndDeletedAtIsNull(
            reviewerId,
            ApprovalStatus.APPROVED
        )
        .orElseThrow(() ->
            new ApiException(
                ErrorResponseCode.USER_APPLICATION_READ_FORBIDDEN
            )
        );

    if (reviewer.getRole() != Role.HUB_MANAGER
        || reviewer.getHubId() == null) {
      throw new ApiException(
          ErrorResponseCode.USER_APPLICATION_READ_FORBIDDEN
      );
    }
    return reviewer;
  }
}
