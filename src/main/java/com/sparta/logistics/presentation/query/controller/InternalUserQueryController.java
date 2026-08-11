package com.sparta.logistics.presentation.query.controller;

import com.sparta.logistics.application.query.dto.UserResponse;
import com.sparta.logistics.application.query.usecase.SearchUserUseCase;
import com.sparta.logistics.common.code.GeneralResponseCode;
import com.sparta.logistics.presentation.common.dto.response.GeneralResponse;
import com.sparta.logistics.presentation.query.request.SearchUsersRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/api/v1/users")
public class InternalUserQueryController {

  private final SearchUserUseCase searchUserUseCase;

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
}
