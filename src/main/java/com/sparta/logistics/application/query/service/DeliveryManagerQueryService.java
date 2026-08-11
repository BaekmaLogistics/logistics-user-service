package com.sparta.logistics.application.query.service;

import com.sparta.logistics.application.query.dto.DeliveryManagerItem;
import com.sparta.logistics.application.query.dto.DeliveryManagerPageResponse;
import com.sparta.logistics.application.query.dto.SearchDeliveryManagerQuery;
import com.sparta.logistics.application.query.usecase.SearchDeliveryManagerUseCase;
import com.sparta.logistics.common.code.ErrorResponseCode;
import com.sparta.logistics.common.exception.ApiException;
import com.sparta.logistics.domain.model.DeliveryManagerType;
import com.sparta.logistics.domain.repository.DeliveryManagerQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryManagerQueryService implements SearchDeliveryManagerUseCase {

  private final DeliveryManagerQueryRepository deliveryManagerQueryRepository;

  @Override
  public DeliveryManagerPageResponse searchDeliveryManagers(SearchDeliveryManagerQuery query) {

    // 업체 배송 담당자는 소속 허브 필요
    validateQuery(query);

    Page<DeliveryManagerItem> responses = deliveryManagerQueryRepository.search(
        query.deliveryManagerType(),
        query.hubId(),
        query.toPageable()
    );

    return DeliveryManagerPageResponse.from(responses);
  }

  // 업체 배송 담당자는 소속 허브 필요
  private void validateQuery(SearchDeliveryManagerQuery query) {
    if (query.deliveryManagerType()
        == DeliveryManagerType.COMPANY_DELIVERY
        && query.hubId() == null) {
      throw new ApiException(
          ErrorResponseCode.DELIVERY_MANAGER_HUB_REQUIRED
      );
    }
  }
}
