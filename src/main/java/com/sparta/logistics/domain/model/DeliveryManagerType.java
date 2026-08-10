package com.sparta.logistics.domain.model;

public enum DeliveryManagerType {

  HUB_DELIVERY,
  COMPANY_DELIVERY;

  public static DeliveryManagerType from(
      RequestedDeliveryType requestedType
  ) {
    return switch (requestedType) {
      case HUB_DELIVERY -> HUB_DELIVERY;
      case COMPANY_DELIVERY -> COMPANY_DELIVERY;
    };
  }
}
