package com.sparta.logistics.domain.repository;

import com.sparta.logistics.domain.entity.User;
import com.sparta.logistics.domain.model.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
  List<User> findAllByIdInAndApprovalStatusAndDeletedAtIsNull(List<UUID> list, ApprovalStatus approvalStatus);

  Optional<User> findByIdAndDeletedAtIsNull(UUID uuid);
}
