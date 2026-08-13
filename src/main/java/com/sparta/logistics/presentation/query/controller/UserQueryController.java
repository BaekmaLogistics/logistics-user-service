package com.sparta.logistics.presentation.query.controller;

import com.sparta.logistics.application.query.dto.GetUserApplicationsQuery;
import com.sparta.logistics.application.query.dto.MyUserResponse;
import com.sparta.logistics.application.query.dto.UserApplicationPageResponse;
import com.sparta.logistics.application.query.usecase.GetUserApplicationsUseCase;
import com.sparta.logistics.application.query.usecase.GetUserUseCase;
import com.sparta.logistics.common.code.GeneralResponseCode;
import com.sparta.logistics.infrastructure.security.GatewayUserPrincipal;
import com.sparta.logistics.presentation.common.dto.response.GeneralResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserQueryController {

  private final GetUserApplicationsUseCase getUserApplicationsUseCase;
  private final GetUserUseCase getUserUseCase;

  @GetMapping("/signup-applications")
  @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
  public ResponseEntity<GeneralResponse<UserApplicationPageResponse>> getUserApplications(
      @AuthenticationPrincipal GatewayUserPrincipal principal,
      @PageableDefault(
          page = 0,
          size = 10,
          sort = "createdAt",
          direction = Sort.Direction.DESC
      )
      Pageable pageable
  ) {

    GetUserApplicationsQuery query =
        new GetUserApplicationsQuery(
            principal.userId(),
            principal.role(),
            pageable
        );

    UserApplicationPageResponse response =
        getUserApplicationsUseCase.getUserApplications(query);

    return GeneralResponse.toResponseEntity(
        GeneralResponseCode.OK,
        response
    );
  }


  @GetMapping("/me")
  public ResponseEntity<GeneralResponse<MyUserResponse>> getMyUser(
      @AuthenticationPrincipal GatewayUserPrincipal principal
  ) {

    MyUserResponse response = getUserUseCase.getMyUser(principal.userId());
    return GeneralResponse.toResponseEntity(
        GeneralResponseCode.OK,
        response
    );
  }
}
