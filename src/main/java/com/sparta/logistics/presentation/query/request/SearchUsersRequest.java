package com.sparta.logistics.presentation.query.request;

import com.sparta.logistics.application.query.dto.SearchUserQuery;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record SearchUsersRequest(

    @NotEmpty(message = "조회할 사용자 ID는 필수입니다.")
    List<UUID> userIds
) {

  public SearchUserQuery toQuery() {
    return new SearchUserQuery(userIds);
  }
}
