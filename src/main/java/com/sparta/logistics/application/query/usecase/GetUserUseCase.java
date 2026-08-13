package com.sparta.logistics.application.query.usecase;

import com.sparta.logistics.application.query.dto.MyUserResponse;

import java.util.UUID;

public interface GetUserUseCase {

  MyUserResponse getMyUser(UUID userId);
}
