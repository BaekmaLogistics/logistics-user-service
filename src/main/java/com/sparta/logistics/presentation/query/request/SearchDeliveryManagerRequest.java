package com.sparta.logistics.presentation.query.request;

import com.sparta.logistics.application.query.dto.SearchDeliveryManagerQuery;
import com.sparta.logistics.domain.model.DeliveryManagerType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SearchDeliveryManagerRequest(

    @NotNull(message = "배송 유형은 필수입니다.")
    DeliveryManagerType deliveryType,

    UUID hubId,

    Integer page,

    Integer size
) {

  public SearchDeliveryManagerRequest {
    page = page == null ? 0 : page;
    size = size == null ? 0 : size;
  }

  public SearchDeliveryManagerQuery toQuery() {
    return new SearchDeliveryManagerQuery(
        deliveryType,
        hubId,
        page,
        size
    );
  }
}

