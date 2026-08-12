package com.sparta.logistics.presentation.query.controller;

import com.sparta.logistics.application.query.dto.UserAccessResponse;
import com.sparta.logistics.application.query.dto.UserResponse;
import com.sparta.logistics.application.query.usecase.GetUserAccessUseCase;
import com.sparta.logistics.application.query.usecase.SearchUserUseCase;
import com.sparta.logistics.common.code.GeneralResponseCode;
import com.sparta.logistics.presentation.common.dto.response.GeneralResponse;
import com.sparta.logistics.presentation.query.request.SearchUsersRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/api/v1/users")
public class InternalUserQueryController {

  private final SearchUserUseCase searchUserUseCase;
  private final GetUserAccessUseCase getUserAccessUseCase;

  @PostMapping("/search")
  public ResponseEntity<GeneralResponse<List<UserResponse>>> searchUsersById(
      @Valid @RequestBody SearchUsersRequest request
      ) {
    List<UserResponse> responses = searchUserUseCase.searchUsers(request.toQuery());

    return GeneralResponse.toResponseEntity(
        GeneralResponseCode.OK,
        responses
    );
  }


  @GetMapping("/{userId}")
  public ResponseEntity<GeneralResponse<UserAccessResponse>> getUserAccess(
      @PathVariable UUID userId
  ) {

    UserAccessResponse response = getUserAccessUseCase.getUserAccess(userId);

    return GeneralResponse.toResponseEntity(
        GeneralResponseCode.OK,
        response
    );
  }
}
