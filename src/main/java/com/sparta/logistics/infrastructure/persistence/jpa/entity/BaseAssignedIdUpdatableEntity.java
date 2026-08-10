package com.sparta.logistics.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseAssignedIdUpdatableEntity {
  @Id
  @Column(columnDefinition = "uuid", updatable = false, nullable = false)
  protected UUID id;

  @CreatedDate
  @Column(name = "created_at", updatable = false, nullable = false)
  private Instant createdAt;

  @CreatedBy
  @Column(name = "created_by", updatable = false)
  private UUID createdBy;

  @LastModifiedDate
  @Column(name = "updated_at")
  private Instant updatedAt;

  @LastModifiedBy
  @Column(name = "updated_by")
  private UUID updatedBy;

  @Column(name = "deleted_at")
  private Instant deletedAt;

  @Column(name = "deleted_by")
  private UUID deletedBy;

  public void softDelete(UUID deletedBy) {
    this.deletedAt = Instant.now();
    this.deletedBy = deletedBy;
  }
}
