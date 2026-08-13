package com.sparta.logistics.domain.repository;

import com.sparta.logistics.domain.entity.User;
import com.sparta.logistics.domain.model.ApprovalStatus;
import com.sparta.logistics.domain.model.RequestedDeliveryType;
import com.sparta.logistics.domain.model.RequestedRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
  List<User> findAllByIdInAndApprovalStatusAndDeletedAtIsNull(List<UUID> list, ApprovalStatus approvalStatus);

  Optional<User> findByIdAndDeletedAtIsNull(UUID uuid);

  Optional<User> findByIdAndApprovalStatusAndDeletedAtIsNull(UUID userId, ApprovalStatus approvalStatus);

  Page<User> findAllByApprovalStatusAndDeletedAtIsNull(ApprovalStatus approvalStatus, Pageable pageable);


  @Query("""
    select u
    from User u
    where u.approvalStatus = :approvalStatus
      AND u.hubId = :hubId
      AND u.deletedAt IS NULL
      AND (
        u.requestedRole <> :deliveryManagerRole
        OR u.requestedDeliveryType IS NULL
        OR u.requestedDeliveryType <> :hubDeliveryType
      )
    """)
  Page<User> findReadableApplicationsByHubManager(
      @Param("approvalStatus")
      ApprovalStatus approvalStatus,
      @Param("hubId")
      UUID hubId,
      @Param("deliveryManagerRole")
      RequestedRole requestedRole,
      @Param("hubDeliveryType")
      RequestedDeliveryType requestedDeliveryType,
      Pageable pageable
  );
}
