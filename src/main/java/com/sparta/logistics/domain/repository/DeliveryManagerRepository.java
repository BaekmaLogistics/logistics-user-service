package com.sparta.logistics.domain.repository;

import com.sparta.logistics.domain.entity.DeliveryManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface DeliveryManagerRepository extends JpaRepository<DeliveryManager, UUID> {

  @Query("""
    select coalesce(max(dm.deliveryOrder), 0)
    from DeliveryManager dm
    where dm.deliveryManagerType = 'HUB_DELIVERY'
    """)
  int findMaxHubDeliveryOrder();

  @Query("""
    select coalesce(max(dm.deliveryOrder), 0) 
    from DeliveryManager dm
    where dm.deliveryManagerType = 'COMPANY_DELIVERY'
      and dm.hubId = :hubId
    """)
  int findMaxCompanyDelivery(
      @Param("hubId") UUID hubId
  );

  boolean existsByUserId(UUID id);
}
