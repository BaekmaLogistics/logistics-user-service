package com.sparta.logistics.application.query.service;

import com.sparta.logistics.application.query.dto.SearchUserQuery;
import com.sparta.logistics.application.query.dto.UserResponse;
import com.sparta.logistics.application.query.usecase.SearchUserUseCase;
import com.sparta.logistics.domain.model.ApprovalStatus;
import com.sparta.logistics.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserQueryService implements SearchUserUseCase {

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
}
