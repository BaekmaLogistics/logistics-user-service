package com.sparta.logistics.application.query.usecase;

import com.sparta.logistics.application.query.dto.DeliveryManagerPageResponse;
import com.sparta.logistics.application.query.dto.SearchDeliveryManagerQuery;

public interface SearchDeliveryManagerUseCase {

  DeliveryManagerPageResponse searchDeliveryManagers(
      SearchDeliveryManagerQuery query
  );
}
