package com.sparta.logistics.application.query.usecase;

import com.sparta.logistics.application.query.dto.UserAccessResponse;

import java.util.UUID;

public interface GetUserAccessUseCase {

  UserAccessResponse getUserAccess(UUID userId);
}
