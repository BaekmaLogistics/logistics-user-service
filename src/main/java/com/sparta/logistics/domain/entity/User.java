package com.sparta.logistics.domain.entity;

import com.sparta.logistics.domain.model.ApprovalStatus;
import com.sparta.logistics.domain.model.RequestedDeliveryType;
import com.sparta.logistics.domain.model.RequestedRole;
import com.sparta.logistics.domain.model.Role;
import com.sparta.logistics.infrastructure.persistence.jpa.entity.BaseAssignedIdUpdatableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Table(name = "p_users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseAssignedIdUpdatableEntity {


  @Column(nullable = false, length = 50)
  private String name;

  @Column(nullable = false, length = 50)
  private String slackId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private RequestedRole requestedRole;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private RequestedDeliveryType requestedDeliveryType;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private Role role;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private ApprovalStatus approvalStatus;

  @Column(name = "hub_id")
  private UUID hubId;

  @Column(name = "company_id")
  private UUID companyId;

  private User (
          UUID id,
          String name,
          String slackId,
          RequestedRole requestedRole,
          RequestedDeliveryType requestedDeliveryType,
          UUID hubId,
          UUID companyId
  ) {

    this.id = id;
    this.name = name;
    this.slackId = slackId;
    this.requestedRole = requestedRole;
    this.requestedDeliveryType = requestedDeliveryType;
    this.hubId = hubId;
    this.companyId = companyId;

    this.role = null;
    this.approvalStatus = ApprovalStatus.PENDING;

  }

  public static User createPending(
          UUID id,
          String name,
          String slackId,
          RequestedRole requestedRole,
          RequestedDeliveryType requestedDeliveryType,
          UUID hubId,
          UUID companyId
  ) {

    return new User(
            id,
            name,
            slackId,
            requestedRole,
            requestedDeliveryType,
            hubId,
            companyId
    );

  }

}
