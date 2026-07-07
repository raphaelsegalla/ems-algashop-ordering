package com.algaworks.algashop.ordering.domain.entity;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    DRAFT,
    PLACED(DRAFT),
    PAID(PLACED),
    READY(PAID),
    CANCELED(PAID, READY, PLACED, DRAFT);

    OrderStatus(OrderStatus... previewStatuses) {
        this.previewStatuses = Arrays.asList(previewStatuses);
    }

    private final List<OrderStatus> previewStatuses;

    public boolean canChangeTo(OrderStatus newStatus) {
        return newStatus.previewStatuses.contains(this);

    }

    public boolean canNotChangeTo(OrderStatus newStatus) {
        return !canChangeTo(newStatus);

    }
}
