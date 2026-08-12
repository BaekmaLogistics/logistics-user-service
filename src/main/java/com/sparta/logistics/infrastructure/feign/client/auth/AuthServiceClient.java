package com.sparta.logistics.infrastructure.feign.client.auth;


import com.sparta.logistics.infrastructure.feign.config.OpenFeignConfig;
import com.sparta.logistics.infrastructure.feign.dto.ActivateAuthAccountRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "auth-service", configuration = OpenFeignConfig.class)
public interface AuthServiceClient {


  @PatchMapping("/internal/api/v1/auth/accounts/{userId}/activate")
  void activateAccount(
      @PathVariable UUID userId,
      @RequestBody ActivateAuthAccountRequest activateAuthAccountRequest
  );

  @PatchMapping("/internal/api/v1/auth/accounts/{userId}/reject")
  void rejectAccount(
      @PathVariable UUID userId
  );
}
