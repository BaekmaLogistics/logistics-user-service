package com.sparta.logistics.infrastructure.security;

import com.sparta.logistics.domain.model.Role;

import java.util.UUID;

public record GatewayUserPrincipal(
    UUID userId,
    Role role
) {
}
