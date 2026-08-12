package com.sparta.logistics.domain.repository;

import com.sparta.logistics.application.query.dto.DeliveryManagerItem;
import com.sparta.logistics.domain.model.DeliveryManagerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface DeliveryManagerQueryRepository {

  Page<DeliveryManagerItem> search(
      DeliveryManagerType deliveryManagerType,
      UUID hubId,
      Pageable pageable
  );
}
