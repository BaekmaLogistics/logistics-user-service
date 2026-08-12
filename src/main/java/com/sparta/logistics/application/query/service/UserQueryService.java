package com.sparta.logistics.application.query.service;

import com.sparta.logistics.application.query.dto.SearchUserQuery;
import com.sparta.logistics.application.query.dto.UserAccessResponse;
import com.sparta.logistics.application.query.dto.UserResponse;
import com.sparta.logistics.application.query.usecase.GetUserAccessUseCase;
import com.sparta.logistics.application.query.usecase.SearchUserUseCase;
import com.sparta.logistics.common.code.ErrorResponseCode;
import com.sparta.logistics.common.exception.ApiException;
import com.sparta.logistics.domain.entity.User;
import com.sparta.logistics.domain.model.ApprovalStatus;
import com.sparta.logistics.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserQueryService implements
    SearchUserUseCase,
    GetUserAccessUseCase
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
}
