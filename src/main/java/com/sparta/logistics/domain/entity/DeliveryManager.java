package com.sparta.logistics.domain.entity;

import com.sparta.logistics.domain.model.DeliveryManagerType;
import com.sparta.logistics.infrastructure.persistence.jpa.entity.BaseAssignedIdUpdatableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Table(name = "p_delivery_managers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryManager extends BaseAssignedIdUpdatableEntity {

  @Column(name = "hub_id")
  private UUID hubId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private DeliveryManagerType deliveryManagerType;

  @Column(nullable = false)
  private Integer deliveryOrder;

  private DeliveryManager(
      UUID id,
      UUID hubId,
      DeliveryManagerType deliveryManagerType,
      Integer deliveryOrder
  ) {
    this.id = id;
    this.hubId = hubId;
    this.deliveryManagerType = deliveryManagerType;
    this.deliveryOrder = deliveryOrder;
  }

  public static DeliveryManager create(
      UUID userId,
      UUID hubId,
      DeliveryManagerType deliveryManagerType,
      Integer deliveryOrder
  ) {

    return new DeliveryManager(
        userId,
        hubId,
        deliveryManagerType,
        deliveryOrder
    );
  }
}
