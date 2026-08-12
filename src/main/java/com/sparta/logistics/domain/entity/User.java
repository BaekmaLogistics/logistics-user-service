package com.sparta.logistics.domain.entity;

import com.sparta.logistics.common.code.ErrorResponseCode;
import com.sparta.logistics.common.exception.ApiException;
import com.sparta.logistics.domain.model.ApprovalStatus;
import com.sparta.logistics.domain.model.RequestedDeliveryType;
import com.sparta.logistics.domain.model.RequestedRole;
import com.sparta.logistics.domain.model.Role;
import com.sparta.logistics.infrastructure.persistence.jpa.entity.BaseAssignedIdUpdatableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
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

  @Column(length = 255)
  private String rejectionReason;

  @Column(name = "reviewed_by")
  private UUID reviewedBy;

  @Column(name = "reviewed_at")
  private Instant reviewedAt;

  private User(
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

  public void approve(UUID reviewerId) {
    validatePendingStatus();
    //승인, 거절이 완료되었는지 확인
    if (this.approvalStatus != ApprovalStatus.PENDING) {
      throw new ApiException(ErrorResponseCode.ALREADY_REVIEWED_USER);
    }

    // null -> role 변경
    this.role = switch (this.requestedRole) {
      case HUB_MANAGER -> Role.HUB_MANAGER;
      case DELIVERY_MANAGER -> Role.DELIVERY_MANAGER;
      case SUPPLIER_MANAGER ->  Role.SUPPLIER_MANAGER;
    };

    this.approvalStatus = ApprovalStatus.APPROVED;
    this.rejectionReason = null;
    this.reviewedBy = reviewerId;
    this.reviewedAt = Instant.now();
  }

  // 승인 거절
  public void reject(String reason, UUID reviewedBy) {
    validatePendingStatus();

    this.role = null;
    this.approvalStatus = ApprovalStatus.REJECTED;
    this.rejectionReason = reason;
    this.reviewedBy = reviewedBy;
    this.reviewedAt = Instant.now();
  }

  // PENDING이 아니면 오류
  private void validatePendingStatus() {
    if (this.approvalStatus != ApprovalStatus.PENDING) {
      throw new ApiException(ErrorResponseCode.ALREADY_REVIEWED_USER);
    }
  }
}
