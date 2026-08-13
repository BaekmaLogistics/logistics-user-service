package com.sparta.logistics.application.query.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record UserApplicationPageResponse(
    List<UserApplicationItem> content,
    int page,
    int size,
    long totalElements,
    int totalPages

) {
  public static UserApplicationPageResponse from(
      Page<UserApplicationItem> result
  ) {
    return new UserApplicationPageResponse(
        result.getContent(),
        result.getNumber(),
        result.getSize(),
        result.getTotalElements(),
        result.getTotalPages()
    );
  }
}
