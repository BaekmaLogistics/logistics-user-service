package com.sparta.logistics.application.query.dto;

import com.sparta.logistics.domain.model.DeliveryManagerType;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public record DeliveryManagerPageResponse(
    List<DeliveryManagerItem> content,
    int page,
    int size,
    long totalElements,
    int totalPage

) {
  public static DeliveryManagerPageResponse from(
      Page<DeliveryManagerItem> result
  ) {
    return new DeliveryManagerPageResponse(
        result.getContent(),
        result.getNumber(),
        result.getSize(),
        result.getTotalElements(),
        result.getTotalPages()
    );
  }
}
