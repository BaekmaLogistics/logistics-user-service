package com.sparta.logistics.application.query.dto;

import java.util.List;
import java.util.UUID;

public record SearchUserQuery(
    List<UUID> userIds
) {
}
