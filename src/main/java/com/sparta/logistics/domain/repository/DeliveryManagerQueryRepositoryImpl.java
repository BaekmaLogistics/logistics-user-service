package com.sparta.logistics.domain.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.logistics.application.query.dto.DeliveryManagerItem;
import com.sparta.logistics.domain.entity.QDeliveryManager;
import com.sparta.logistics.domain.entity.QUser;
import com.sparta.logistics.domain.model.ApprovalStatus;
import com.sparta.logistics.domain.model.DeliveryManagerType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DeliveryManagerQueryRepositoryImpl implements DeliveryManagerQueryRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<DeliveryManagerItem> search(DeliveryManagerType deliveryManagerType, UUID hubId, Pageable pageable) {

    QDeliveryManager deliveryManager = QDeliveryManager.deliveryManager;
    QUser user = QUser.user;

    // 배송 유형, 허브, 승인 상태, 논리 삭제 여부 등의 조회 조건 생성
    BooleanBuilder conditions = createConditions(
        deliveryManagerType,
        hubId,
        deliveryManager,
        user
    );

    // 페이지에 포함될 배송 담당자 목록
    List<DeliveryManagerItem> content = queryFactory
        .select(Projections.constructor(
            DeliveryManagerItem.class,

            deliveryManager.id,
            user.name,
            user.slackId,
            deliveryManager.deliveryManagerType,
            deliveryManager.hubId,
            deliveryManager.deliveryOrder
        ))
        // DeliveryManager와 User 함께 조회
        .from(deliveryManager, user)
        .where(conditions)
        // 배송 순번이 빠른 담당자부터
        .orderBy(deliveryManager.deliveryOrder.asc())
        // 시작 위치
        .offset(pageable.getOffset())
        // 한 페이지 조회할 개수
        .limit(pageable.getPageSize())
        .fetch();

    // 전체
    Long total = queryFactory
        .select(deliveryManager.count())
        .from(deliveryManager, user)
        .where(conditions)
        .fetchOne();


    return new PageImpl<>(
        content,
        pageable
        ,total == null ? 0L : total
    );
  }

  private BooleanBuilder createConditions(
      DeliveryManagerType deliveryManagerType,
      UUID hubId,
      QDeliveryManager deliveryManager,
      QUser user
  ) {

    BooleanBuilder builder = new BooleanBuilder()

        .and(deliveryManager.id.eq(user.id))
        //배송 담당자 유형 조회
        .and(deliveryManager.deliveryManagerType.eq(deliveryManagerType))
        .and(deliveryManager.deletedAt.isNull())
        .and(user.deletedAt.isNull())
        // 가입 승인이 완료된 계정만 조회
        .and(user.approvalStatus.eq(ApprovalStatus.APPROVED));


    // 허브 배송 담당자는 허브에 소속되지 않음
    if (deliveryManagerType == DeliveryManagerType.HUB_DELIVERY) {
      builder.and(deliveryManager.hubId.isNull());
    }
    // 업체 배송 담당자는 허브에 소속
    else {
      builder.and(deliveryManager.hubId.eq(hubId));
    }
    return builder;
  }
}
