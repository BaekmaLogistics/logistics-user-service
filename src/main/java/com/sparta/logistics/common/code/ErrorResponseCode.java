package com.sparta.logistics.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorResponseCode implements ApiResponseCode {
    // Common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"COMMON_0001", "알 수 없는 오류가 발생했습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "COMMON_0002","유효하지 않은 요청입니다."),
    FEIGN_CLIENT_ERROR(HttpStatus.BAD_GATEWAY, "COMMON_0003", "Feign 통신 중 오류가 발생했습니다."),

    DUPLICATE_USER(HttpStatus.CONFLICT, "USER_0001", "이미 생성된 사용자입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_0002", "사용자를 찾을 수 없습니다."),
    ALREADY_REVIEWED_USER(HttpStatus.CONFLICT, "USER_0003", "이미 완료된 승인입니다."),
    USER_APPROVAL_FORBIDDEN(HttpStatus.FORBIDDEN, "USER_0004", "해당 사용자의 가입을 승인할 권한이 없습니다."),
    DUPLICATE_DELIVERY_MANAGER(HttpStatus.CONFLICT, "USER_0005", "이미 생성된 배송 담당자입니다"),
    DELIVERY_MANAGER_LIMIT_EXCEEDED(HttpStatus.CONFLICT, "USER_0006", "배송 담당자 최대 인원을 초과했습니다."),
    DELIVERY_MANAGER_HUB_REQUIRED(HttpStatus.BAD_REQUEST, "USER_0007", "업체 배송 담당자는 소속 허브가 필요합니다."),

    USER_APPLICATION_READ_FORBIDDEN(HttpStatus.FORBIDDEN, "USER_0008", "가입 신청 목록을 조회할 권한이 없습니다.");

    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}
