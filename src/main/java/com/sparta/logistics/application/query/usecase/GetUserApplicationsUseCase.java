package com.sparta.logistics.application.query.usecase;

import com.sparta.logistics.application.query.dto.GetUserApplicationsQuery;
import com.sparta.logistics.application.query.dto.UserApplicationPageResponse;

public interface GetUserApplicationsUseCase {

  UserApplicationPageResponse getUserApplications(
      GetUserApplicationsQuery query
  );
}
