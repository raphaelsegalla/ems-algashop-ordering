package com.algaworks.algashop.ordering.domain.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderStatusTest {

    @Test
    public void canChangeTo() {
        Assertions.assertThat(OrderStatus.DRAFT.canChangeTo(OrderStatus.PLACED)).isTrue();
        Assertions.assertThat(OrderStatus.DRAFT.canChangeTo(OrderStatus.CANCELED)).isTrue();
        Assertions.assertThat(OrderStatus.DRAFT.canChangeTo(OrderStatus.PAID)).isFalse();
        Assertions.assertThat(OrderStatus.DRAFT.canChangeTo(OrderStatus.READY)).isFalse();

        Assertions.assertThat(OrderStatus.PLACED.canChangeTo(OrderStatus.PAID)).isTrue();
        Assertions.assertThat(OrderStatus.PLACED.canChangeTo(OrderStatus.CANCELED)).isTrue();
        Assertions.assertThat(OrderStatus.PLACED.canChangeTo(OrderStatus.DRAFT)).isFalse();
        Assertions.assertThat(OrderStatus.PLACED.canChangeTo(OrderStatus.READY)).isFalse();

        Assertions.assertThat(OrderStatus.PAID.canChangeTo(OrderStatus.READY)).isTrue();
        Assertions.assertThat(OrderStatus.PAID.canChangeTo(OrderStatus.CANCELED)).isTrue();
        Assertions.assertThat(OrderStatus.PAID.canChangeTo(OrderStatus.DRAFT)).isFalse();
        Assertions.assertThat(OrderStatus.PAID.canChangeTo(OrderStatus.PLACED)).isFalse();

        Assertions.assertThat(OrderStatus.READY.canChangeTo(OrderStatus.CANCELED)).isTrue();
        Assertions.assertThat(OrderStatus.READY.canChangeTo(OrderStatus.DRAFT)).isFalse();
        Assertions.assertThat(OrderStatus.READY.canChangeTo(OrderStatus.PLACED)).isFalse();
        Assertions.assertThat(OrderStatus.READY.canChangeTo(OrderStatus.PAID)).isFalse();
    }

    @Test
    public void canNotChangeTo() {
        Assertions.assertThat(OrderStatus.PLACED.canNotChangeTo(OrderStatus.DRAFT)).isTrue();
    }

}