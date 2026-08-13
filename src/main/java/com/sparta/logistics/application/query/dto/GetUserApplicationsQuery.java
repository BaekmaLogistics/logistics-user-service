package com.sparta.logistics.application.query.dto;

import com.sparta.logistics.domain.model.Role;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public record GetUserApplicationsQuery(
    UUID reviewerId,
    Role reviewerRole,
    Pageable pageable

) {
}
