package com.sparta.logistics.presentation.query.controller;

import com.sparta.logistics.application.query.dto.DeliveryManagerPageResponse;
import com.sparta.logistics.application.query.usecase.SearchDeliveryManagerUseCase;
import com.sparta.logistics.common.code.GeneralResponseCode;
import com.sparta.logistics.presentation.common.dto.response.GeneralResponse;
import com.sparta.logistics.presentation.query.request.SearchDeliveryManagerRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/api/v1/delivery-managers")
public class InternalDeliveryManagerQueryController {

  private final SearchDeliveryManagerUseCase searchDeliveryManagerUseCase;

  @GetMapping
  public ResponseEntity<
      GeneralResponse<DeliveryManagerPageResponse>> searchDeliveryManagers(
      @Valid @ModelAttribute SearchDeliveryManagerRequest request
      ) {

    DeliveryManagerPageResponse response =
        searchDeliveryManagerUseCase.searchDeliveryManagers(request.toQuery());

    return GeneralResponse.toResponseEntity(
        GeneralResponseCode.OK,
        response
    );
  }
}
