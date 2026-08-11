package com.sparta.logistics.application.query.usecase;

import com.sparta.logistics.application.query.dto.SearchUserQuery;
import com.sparta.logistics.application.query.dto.UserResponse;

import java.util.List;

public interface SearchUserUseCase {

  List<UserResponse> searchUsers(SearchUserQuery query);



}
