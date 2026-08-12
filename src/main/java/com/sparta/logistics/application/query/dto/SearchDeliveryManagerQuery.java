package com.sparta.logistics.application.query.dto;

import com.sparta.logistics.domain.model.DeliveryManagerType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public record SearchDeliveryManagerQuery(

    DeliveryManagerType deliveryType,
    UUID hubId,
    Integer page,
    Integer size
) {

  public Pageable toPageable() {
    return PageRequest.of(page, size);
  }
}
